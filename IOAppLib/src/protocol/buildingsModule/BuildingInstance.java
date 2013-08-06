package protocol.buildingsModule;

import java.io.Serializable;

public class BuildingInstance implements Serializable {
    
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildingInstance other = (BuildingInstance) obj;
		if (column != other.column)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
