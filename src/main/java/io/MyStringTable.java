package io;

import util.collections.Couple;

import java.io.*;
import java.util.*;


//TODO Checking if adding
public class MyStringTable {

    private int currentCellCounter = 0;
    List<String> colNames;
    List<List<String>> contents;
    Random rnd = new Random();

    public static MyStringTable createEmpty(int nCols) {
        return new MyStringTable(0, nCols);
    }

    public static MyStringTable fromColumnArray(String[] columns) {
        MyStringTable result = new MyStringTable(0, columns.length);
        for (int i = 0; i < columns.length; i++) {
            result.setColName(i, columns[i]);
        }
        return result;
    }

    public static MyStringTable fromFile(String path, boolean hasColumnNames,
                                         String separator) {
        System.out.println("Trying to load resource: " + path);
        String prefix = MyStringTable.class.getClassLoader().getResource("").getFile();
        String fileName = prefix + path;

        File file = new File(fileName);

        try {
            return new MyStringTable(file, hasColumnNames, separator);
        } catch (IOException e) {
            System.err.println("Couldn't find file " + fileName);
            return null;
        }

    }

    public static MyStringTable mergeMultiple(List<MyStringTable> originals) {
        // Get column names from first original
        MyStringTable first = originals.get(0);
        int nCols = first.getNumCols();
        MyStringTable result = new MyStringTable(0, nCols);
        // Copy column names
        for (int i = 0; i < nCols; i++) {
            String currCol = first.getColName(i);
            result.setColName(i, currCol);
        }
        for (MyStringTable mst : originals) {
            for (List<String> l : mst.contents) {
                result.contents.add(l);
            }
        }
        return result;
    }

    MyStringTable() {
        contents = new ArrayList<List<String>>();
    }

    MyStringTable(File input, boolean firstRowAreColumnNames, String separator)
            throws IOException {
        this();

        BufferedReader reader = new BufferedReader(new FileReader(input));

        String firstLine = reader.readLine();
        String[] firstEntries = firstLine.split(separator);
        initColNames(firstEntries.length);
        if (firstRowAreColumnNames) {
            colNames = new ArrayList<String>(Arrays.asList(firstEntries));
            for (String s : colNames) {
                System.out.print(s + "\t");
            }
        } else {
            colNames = null;
            contents.add(new ArrayList<String>(Arrays.asList(firstEntries)));
        }
        String nextLine;

        while ((nextLine = reader.readLine()) != null) {
            String[] next = nextLine.split(separator);
            contents.add(new ArrayList<String>(Arrays.asList(next)));
        }
        reader.close();

    }

    MyStringTable(int rows, int cols) {
        this();

        for (int i = 0; i < rows; i++) {
            List<String> currRow = new ArrayList<String>(cols);
            for (int j = 0; j < cols; j++) {
                currRow.add("0");
            }
            contents.add(currRow);
        }
        initColNames(cols);
    }

    public void appendColumn(String colName) {
        colNames.add(colName);
        for (List<String> l : contents) {
            l.add(null);
        }
    }

    public void appendEmptyRow() {
        List<String> emptyStringList = new ArrayList<String>(getNumCols());
        for (int i = 0; i < getNumCols(); i++) {
            emptyStringList.add("");
        }
        contents.add(emptyStringList);
    }

    public void appendRow(List<String> newRow) throws IndexOutOfBoundsException {
        if (newRow.size() > getNumCols())
            throw new IndexOutOfBoundsException();
        contents.add(newRow);
    }

    public void deleteColumn(int colNum) {
        String colName = getColName(colNum);
        colNames.remove(colName);
        for (List<String> row : contents) {
            row.remove(colNum);
        }
    }

    public void deleteColumn(String colName) {
        colNames.remove(colName);
        deleteColumn(findColumn(colName));
    }

    public void fillColumn(String colName, String toFill) {
        int colNum = findColumn(colName);
        if (colNum >= 0) {
            for (List<String> l : contents) {
                l.set(colNum, toFill);
            }
        } else {
            System.exit(0);
        }
    }

    public int findColumn(String colName) {
        for (int i = 0; i < colNames.size(); i++) {
            String currCol = colNames.get(i);
            if (currCol.equals(colName)) {
                return i;
            }
        }
        return -1;
    }

    public String getColName(int colNum) {
        return colNames.get(colNum);
    }

    public List<String> getColumnContents(int colNum) {
        List<String> result = new ArrayList<String>(getNumRows());
        for (int i = 0; i < getNumRows(); i++) {
            result.add(getString(i, colNum));
        }
        return result;
    }

    public String[][] getContentsAsStringMatrix() {
        String[][] result = new String[getNumRows()][getNumCols()];
        for (int i = 0; i < getNumRows(); i++) {
            List<String> currRow = contents.get(i);
            for (int j = 0; j < getNumCols(); j++) {
                result[i][j] = currRow.get(j);
            }
        }
        return result;
    }

    public double getDouble(int row, int col) {
        return Double.parseDouble(getString(row, col));
    }

    public double getDouble(int row, String colName) {
        int col = findColumn(colName);
        return Double.parseDouble(getString(row, col));
    }

    public int getFirstIndexOf(String value, int colNum, int startAt) {
        for (int i = startAt; i < getNumRows(); i++) {
            String check = getString(i, colNum);
            if (check.equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public int[] getIndicesOf(String value, int colNum) {
        ArrayList<Integer> tempIndices = new ArrayList<Integer>();
        for (int i = 0; i < getNumRows(); i++) {
            String check = getString(i, colNum);
            if (check.equals(value)) {
                tempIndices.add(i);
            }
        }
        int[] result = new int[tempIndices.size()];
        for (int i = 0; i < tempIndices.size(); i++) {
            result[i] = tempIndices.get(i);
        }
        return result;
    }

    public int getInteger(int row, int col) {
        return Integer.parseInt(getString(row, col));
    }

    public int getNumCols() {
        return colNames.size();
    }

    public int getNumRows() {
        return contents.size();
    }


    public String getRandomEntry(int colNum) {
        int rowNum = rnd.nextInt(getNumRows());
        return contents.get(rowNum).get(colNum);
    }


    public String getRandomEntry(String colName) {
        int colNum = findColumn(colName);
        return getRandomEntry(colNum);
    }

    public int getRandomRowNum() {
        return rnd.nextInt(getNumRows());
    }

    public String getString(int row, int col) {
        return contents.get(row).get(col);
    }

    public String getString(int row, String colName) {
        int colNum = findColumn(colName);
        if (colNum < 0) {
            System.err.println("Couldn't find column " + colName + "!");
        }
        return getString(row, colNum);
    }

    public List<Couple<String>> getStringPairs(int leftCol, int rightCol) {
        List<Couple<String>> result = new ArrayList<Couple<String>>();
        for (List<String> row : contents) {
            String leftString = row.get(leftCol);
            String rightString = row.get(rightCol);
            result.add(Couple.of(leftString, rightString));
        }
        return result;
    }

    public List<Couple<String>> getStringPairs(String leftCol, String rightCol) {
        int l = findColumn(leftCol);
        int r = findColumn(rightCol);
        return getStringPairs(l, r);
    }

    public ArrayList<String> getStrings(int[] rows, int col) {
        ArrayList<String> result = new ArrayList<String>(rows.length);
        for (int i : rows) {
            result.add(getString(i, col));
        }
        return result;

    }

    public List<String> getUniqueValues(int colNum) {
        List<String> tempResult = getColumnContents(colNum);
        HashSet<String> tempSet = new HashSet<String>(tempResult);
        return new ArrayList<String>(tempSet);
    }

    public List<String> getUniqueValues(String colName) {
        int colNum = findColumn(colName);
        if (colNum >= 0)
            return getUniqueValues(colNum);
        return Collections.emptyList();
    }

    public boolean hasColumn(String string) {
        return (findColumn(string) >= 0);
    }

    public void print() {
        // Print column names
        for (int i = 0; i < getNumCols(); i++) {
            System.out.print(getColName(i));
            if (i < getNumCols() - 1) {
                System.out.print("\t");
            } else {
                System.out.println();
            }
        }
        // Print contents
        for (int iRow = 0; iRow < getNumRows(); iRow++) {
            for (int iCol = 0; iCol < getNumCols(); iCol++) {

                System.out.print(getString(iRow, iCol));
                if (iCol < getNumCols() - 1) {
                    System.out.print("\t");
                } else {
                    System.out.println();
                }
            }
        }

    }

    public void setColName(int colNum, String name) {
        colNames.set(colNum, name);
    }

    public void setString(int row, int col, String toPut)
            throws IndexOutOfBoundsException {
        if (row > getNumRows() || col > getNumCols())
            throw new IndexOutOfBoundsException();
        contents.get(row).set(col, toPut);
    }

    public void setString(int rowNum, String colName, String toSet) {
        int colNum = findColumn(colName);
        setString(rowNum, colNum, toSet);

    }

    public void setStringInNextCell(String... s) {
        for (String toPut : s) {
            setStringsInNextCell(toPut);
        }
    }

    public void writeToFile(String path, String separator) throws IOException {
        File outputFile = new File(path);
        if (!outputFile.exists()) {

            outputFile.createNewFile();

        }

        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        int nCols = getNumCols();
        for (int i = 0; i < nCols; i++) {
            bw.write(colNames.get(i));
            if (i < nCols - 1)
                bw.write(separator);
            else
                bw.write("\n");
        }
        for (List<String> next : contents) {
            for (int i = 0; i < nCols; i++) {
                bw.write(next.get(i));
                if (i < nCols - 1)
                    bw.write(separator);
                else
                    bw.write("\n");
            }
        }

        bw.flush();
        bw.close();

    }

    private void initColNames(int cols) {
        System.out.println("Initializing " + cols + " columns");
        colNames = new ArrayList<String>(cols);
        for (int i = 0; i < cols; i++) {
            colNames.add("<NO NAME>");
        }
    }

    private void setStringsInNextCell(String s) {
        if (currentCellCounter >= getNumCols())
            currentCellCounter = 0;
        if (currentCellCounter == 0)
            this.appendEmptyRow();
        int currentRow = getNumRows() - 1;
        setString(currentRow, currentCellCounter++, s);
    }


}
