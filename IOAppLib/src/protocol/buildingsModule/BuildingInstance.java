package protocol.buildingsModule;

public class BuildingInstance {
    
    private int row;
    private int column;
    private BuildingData data;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public BuildingData getData() {
        return data;
    }

    public void setData(BuildingData data) {
        this.data = data;
    }

}
