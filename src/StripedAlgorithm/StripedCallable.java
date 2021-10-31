package StripedAlgorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class StripedCallable implements Callable<Map<String, Number>> {

    private int[] row;
    private int[] column;
    private int rowIndex;
    private int columnIndex;

    public StripedCallable(int[] row, int[] column, int rowIndex, int columnIndex) {
        this.row = row;
        this.column = column;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    @Override
    public Map<String, Number> call() throws Exception {

        Map<String, Number> resultMap = new HashMap<>();

        int value = 0;
        for (int i = 0; i < row.length; i++){
            value += row[i] * column[i];
        }

        resultMap.put("cellValue", value);
        resultMap.put("rowIndex", rowIndex);
        resultMap.put("columnIndex", columnIndex);

        return resultMap;
    }
}
