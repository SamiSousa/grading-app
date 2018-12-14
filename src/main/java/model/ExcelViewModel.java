package model;

import java.util.Set;

public class ExcelViewModel {
    private String[] cols;
    private Object[][] data;
    private Set<String> filters;
    private int[] stuId;

    public ExcelViewModel(String[] cols, Object[][] data, Set<String> filters) {
        this.cols = cols;
        this.data = data;
        this.filters = filters;
    }

    public void setStuId(int[] stuId) {
        this.stuId = stuId;
    }

    public int[] getStuId() {
        return stuId;
    }

    public Set<String> getFilters() {
        return filters;
    }

    public String[] getCols() {
        return cols;
    }
    public int getColCounts(){
        return cols.length;
    }
    public Object[][] getData() {
        return data;
    }

    @Override
    public String toString() {
        String res = "Cols: ";
        for(String s:cols){
            res += s+" ";
        }
        res += "\n";
        for(Object[] list:data){
            for(Object obj:list){
                res += obj.toString() + " ";
            }
            res += "\n";
        }
        return res;
    }
}
