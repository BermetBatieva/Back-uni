package com.example.Backuni.entity;

public enum BuildingType {
    SERVICE(1),//на обслуживании
    SELFSERVICE(2);//само

    private final int id;

    private int getId() {
        return id;
    }

    BuildingType(int id) {
        this.id = id;
    }

    public static BuildingType getType(int id ) {
        for (BuildingType s : BuildingType.values())
            if (s.getId() == id)
                return s;
        return null;
    }
}
