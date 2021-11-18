package com.example.Backuni.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.Backuni.dto.*;
import com.example.Backuni.entity.Building;
import com.example.Backuni.entity.Cabinet;
import com.example.Backuni.entity.Image;
import com.example.Backuni.entity.Status;
import com.example.Backuni.exception.AlreadyExistException;
import com.example.Backuni.exception.ResourceNotFoundException;
import com.example.Backuni.repository.BuildingRepository;
import com.example.Backuni.repository.CabinetPaginationRepository;
import com.example.Backuni.repository.CabinetRepository;
import com.example.Backuni.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
public class CabinetService {

    @Autowired
    private CabinetRepository cabinetRepository;

    @Autowired
    private CabinetPaginationRepository cabinetPaginationRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private ImageRepository imageRepository;



    public Cabinet add(CabinetDto cabinetDto) {
            Cabinet cabinet = new Cabinet();
            return saverCabinet(cabinet, cabinetDto);
    }

    @Transactional
    Cabinet saverCabinet(Cabinet cabinet, CabinetDto cabinetDto) {
        cabinet.setBuilding(buildingRepository.findById(cabinetDto.getBuildingId())
                .orElseThrow(()-> new ResourceNotFoundException("здание с таким id не найден")));
        cabinet.setStatus(Status.ACTIVATE);
        cabinet.setNumber(cabinetDto.getNumber());
        cabinet.setDescription(cabinetDto.getDescription());
//        cabinet.setImage(cabinetDto.getImage());
        cabinet.setFloorNumber(cabinetDto.getFloorNumber());
//        cabinet.setImage(cabinetDto.getImage());
        cabinetRepository.save(cabinet);
        return cabinet;
    }

    public List<ListCabinets> getAllCabinetsByBuildingIdAndFloorNum(CabinetsByBuildingIdAndFloorNum ids){
        List<Cabinet> cabinets = cabinetRepository.findByBuilding_IdAndFloorNumberAndStatus(ids.getBuildingId(),
                ids.getFloorNum(),Status.ACTIVATE);

        List<ListCabinets> result = new ArrayList<>();

        for(Cabinet cabinet : cabinets){
            ListCabinets model = new ListCabinets();
            model.setId(cabinet.getId());

            List<String> url = new ArrayList<>();
            List<Image> imageList = imageRepository.findByBuilding_IdAndCabinet_Id
                    (cabinet.getBuilding().getId(),cabinet.getId());
            for(Image i : imageList ){
                url.add(i.getUrl());
            }
            model.setUrl(url);
            model.setNumber(cabinet.getNumber());
            model.setDescription(cabinet.getDescription());
            result.add(model);
        }
        return result;
    }

    public CabinetDto getById(Long id){
        Cabinet cabinet = cabinetRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("нет кабинета с таким id = ",id));
        CabinetDto cabinetDto = new CabinetDto();
        cabinetDto.setName(cabinet.getName());
        cabinetDto.setFloorNumber(cabinet.getFloorNumber());
//        cabinetDto.setImage(cabinet.getImage());

        List<String>  url = new ArrayList<>();
        List<Image> imageList = imageRepository.findByBuilding_IdAndCabinet_Id(cabinet.getBuilding().getId(),id);

        for(Image i : imageList ){
            url.add(i.getUrl());
        }
        cabinetDto.setUrlImage(url);
        cabinetDto.setDescription(cabinet.getDescription());
        cabinetDto.setNumber(cabinet.getNumber());
        return cabinetDto;
    }

    @Transactional
    public DeletedDTO deleteById(Long id) {
        Cabinet cabinet = cabinetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("cabinet с таким id не существует! id = ", id));
        cabinet.setStatus(Status.DELETED);
        cabinetRepository.save(cabinet);
        return new DeletedDTO(id);
    }

    public Cabinet updateById(Long id,CabinetDto cabinetDto) {
        Cabinet cabinet = cabinetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("cabinet с таким id не существует! id = ", id));
        cabinet.setStatus(Status.ACTIVATE);
//        cabinet.setImage(cabinetDto.getImage());
        cabinet.setDescription(cabinetDto.getDescription());
        cabinet.setName(cabinetDto.getName());
        cabinet.setName(cabinetDto.getName());
        cabinet.setFloorNumber(cabinetDto.getFloorNumber());
        cabinet.setNumber(cabinetDto.getNumber());
        cabinetRepository.save(cabinet);

        return cabinet;
    }


    private CabinetDto convertToBuildingModel(Cabinet cabinet){
            CabinetDto cabinetDto = new CabinetDto();

            cabinetDto.setId(cabinet.getId());
            cabinetDto.setName(cabinet.getName());
            cabinetDto.setNumber(cabinet.getNumber());
//            cabinetDto.setImage(cabinet.getImage());
            cabinetDto.setFloorNumber(cabinet.getFloorNumber());

            return cabinetDto;
        }

    public Page<CabinetDto> getAllCabinets(Long buildId,Integer floorNum,Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        List<Cabinet> buildingList = cabinetRepository.findByBuilding_IdAndFloorNumberAndStatus(buildId,floorNum,Status.ACTIVATE);
        Page<Cabinet> buildingPage = cabinetPaginationRepository.findAll(pageable);
        List<CabinetDto> transactionModelList = new ArrayList<>();

        buildingPage.forEach(building -> {
            CabinetDto model = convertToBuildingModel(building);

            transactionModelList.add(model);
        });

        return new PageImpl<>(transactionModelList, PageRequest.of(pageNo, pageSize, Sort.by(sortBy)), buildingList.size());
    }


    public ResponseEntity<Cabinet> setImage(MultipartFile[] files, Long cabinetId,Long idBuild) throws IOException {
        final String urlKey = "cloudinary://513184318945249:-PXAzPrMMtx1J7NCL1afdr59new@neobis/";
        List<Image> images = new ArrayList<>();
        Cabinet cabinet = cabinetRepository.findByIdAndBuilding_Id(cabinetId,idBuild);
        Arrays.asList(files).forEach(file -> {

            File file1;
            try {
                file1 = Files.createTempFile(System.currentTimeMillis() + "",
                                Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().length() - 4))
                        .toFile();
                file.transferTo(file1);

                Cloudinary cloudinary = new Cloudinary(urlKey);
                Map uploadResult = cloudinary.uploader().upload(file1, ObjectUtils.emptyMap());

                Image image = new Image();
                image.setCabinet(cabinet);
                image.setBuilding(cabinet.getBuilding());
                image.setName((String) uploadResult.get("public_id"));
                image.setUrl((String) uploadResult.get("url"));
                image.setFormat((String) uploadResult.get("format"));

                images.add(imageRepository.save(image));



            } catch (IOException e) {
                try {
                    throw new IOException("failed to install image");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return ResponseEntity.ok().body(cabinet);
    }


}
