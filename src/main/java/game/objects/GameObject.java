package game.objects;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    @JsonProperty("position")
    private Coords presentPosition;
    private  double direction;

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getDirection() {
        return direction;
    }

    public void setPresentPosition(Coords presentPosition) {
        this.presentPosition = presentPosition;
    }

    public Coords getPresentPosition() {
        return presentPosition;
    }
}

