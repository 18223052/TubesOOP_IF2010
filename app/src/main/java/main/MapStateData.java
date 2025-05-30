package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import object.TileSaveData;

public class MapStateData implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<TileSaveData> tileDataList;

    public MapStateData(){
        this.tileDataList = new ArrayList<>();
    }
}
