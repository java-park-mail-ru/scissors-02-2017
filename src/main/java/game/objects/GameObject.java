package game.objects;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameObject {
    @JsonProperty("position")
    private Coords presentPosition;
    @JsonIgnore
    private Coords desirablePosition;

    private  double direction;

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getDirection() {
        return direction;
    }

    public void setDesirablePosition(Coords desirablePosition) {
        this.desirablePosition = desirablePosition;
    }

    public void setPresentPosition(Coords presentPosition) {
        this.presentPosition = presentPosition;
    }

    public Coords getDesirablePosition() {
        return desirablePosition;
    }

    public Coords getPresentPosition() {
        return presentPosition;
    }
}

