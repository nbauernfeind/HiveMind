package hivemind;

public class Location {
    final double x;
    final double y;
    final int row;
    final int col;

    Location(double x, double y) {
        this.x = x;
        this.y = y;
        row = (int) Math.floor((y - MapFrame.POINT_OFFSET_Y) / MapFrame.POINTS_PER_TILE);
        col = (int) Math.floor((x - MapFrame.POINT_OFFSET_X) / MapFrame.POINTS_PER_TILE);
    }

    Location(int row, int col) {
        this.row = row;
        this.col = col;
        x = MapFrame.POINT_OFFSET_X + col * MapFrame.POINTS_PER_TILE;
        y = MapFrame.POINT_OFFSET_Y + row * MapFrame.POINTS_PER_TILE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location) o;
        return !(col != location.col || row != location.row);
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }
}
