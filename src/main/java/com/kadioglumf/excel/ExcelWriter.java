package com.kadioglumf.excel;

import com.kadioglumf.excel.annotation.Excel;
import com.kadioglumf.excel.annotation.ExcelColumn;
import com.kadioglumf.excel.model.BaseExcel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class ExcelWriter
{
    //private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public <T extends BaseExcel> void write(File file, List<T> list, Class<T> clazz) throws IOException, Exception
    {
        XSSFWorkbook workbook;
        FileInputStream fileInputStream = null;
        try
        {
            if (file.exists())
            {
                fileInputStream = new FileInputStream(file);
                workbook = new XSSFWorkbook(fileInputStream);
            }
            else
            {
                workbook = new XSSFWorkbook();
            }
            Excel excelAnnotation = clazz.getAnnotation(Excel.class);
            if (excelAnnotation != null)
            {
                validate(excelAnnotation);
                String sheetName = excelAnnotation.sheetName();
                clearSheet(workbook, sheetName);
                createNewSheetAndFill(list, clazz, workbook, excelAnnotation, sheetName);
                writeFileOut(file, workbook, fileInputStream);
            }
            else
            {
                throw new Exception(clazz.getName() + " Class does not have @Excel annotation");
            }
        }
        finally
        {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    public <T extends BaseExcel> XSSFWorkbook writeOnWorkbook(List<T> list, Class<T> clazz) throws Exception
    {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Excel excelAnnotation = clazz.getAnnotation(Excel.class);
        if (excelAnnotation != null)
        {
            validate(excelAnnotation);
            String sheetName = excelAnnotation.sheetName();
            clearSheet(workbook, sheetName);
            createNewSheetAndFill(list, clazz, workbook, excelAnnotation, sheetName);
        }
        else
        {
            throw new Exception(clazz.getName() + " Class does not have @Excel annotation");
        }
        return workbook;
    }

    private <T extends BaseExcel> void createNewSheetAndFill(List<T> list, Class<T> clazz, XSSFWorkbook workbook, Excel excelAnnotation, String sheetName)
    {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        createHeader(sheet, excelAnnotation, clazz);
        TreeMap<Integer, Field> fieldsMap = getFieldsMap(clazz);
        for (T t : list)
        {
            XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            for (Integer integer : fieldsMap.keySet())
            {
                XSSFCell cell = row.createCell(integer + excelAnnotation.firstCol());
                setCellValue(cell, fieldsMap.get(integer), t);
            }
        }
    }

    private void clearSheet(XSSFWorkbook workbook, String sheetName)
    {
        int sheetIndex = workbook.getSheetIndex(sheetName);
        if (sheetIndex != -1)
        {
            workbook.removeSheetAt(sheetIndex);
        }
    }

    private void writeFileOut(File file, XSSFWorkbook workbook, FileInputStream fileInputStream) throws IOException
    {
        FileOutputStream outFile = null;
        try
        {
            if (fileInputStream != null)
            {
                fileInputStream.close();
            }
            outFile = new FileOutputStream(file);
            workbook.write(outFile);

        }
        catch (Exception e)
        {
            //logger.error("ExcelWriter.writeFileOut metodunda hata olustu", e);
        }
        finally
        {
            if (outFile != null) {
                outFile.close();
            }
        }
    }

    private TreeMap<Integer, Field> getFieldsMap(Class<?> clazz)
    {
        TreeMap<Integer, Field> fieldsMap = new TreeMap<>();
        List<Field> fieldsListWithAnnotation = FieldUtils.getFieldsListWithAnnotation(clazz, ExcelColumn.class);
        for (Field field : fieldsListWithAnnotation)
        {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            fieldsMap.put(annotation.col(), field);
        }
        return fieldsMap;
    }

    private void createHeader(XSSFSheet sheet, Excel excelAnnotation, Class<?> clazz)
    {
        XSSFRow row = sheet.createRow(excelAnnotation.firstRow() - 1);
        TreeMap<Integer, String> headerNames = getHeaderNames(clazz);
        for (Integer integer : headerNames.keySet())
        {
            XSSFCell cell = row.createCell(integer + excelAnnotation.firstCol());
            cell.setCellType(CellType.STRING);
            cell.setCellValue(headerNames.get(integer));
        }
    }

    private TreeMap<Integer, String> getHeaderNames(Class<?> clazz)
    {
        TreeMap<Integer, String> headerMap = new TreeMap<>();
        List<Field> fieldsListWithAnnotation = FieldUtils.getFieldsListWithAnnotation(clazz, ExcelColumn.class);
        for (Field field : fieldsListWithAnnotation)
        {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            headerMap.put(annotation.col(), annotation.columnName());
        }
        return headerMap;
    }

    private <T extends BaseExcel> void setCellValue(XSSFCell cell, Field field, T t)
    {
        Class<?> type = field.getType();
        boolean accessible = field.isAccessible();
        AccessibleObject.setAccessible(new AccessibleObject[] {field}, true);
        try
        {
            if (type == String.class)
            {
                cell.setCellType(CellType.STRING);
                String value = (String) field.get(t);
                cell.setCellValue(value);
            }
            else if (type == Integer.class)
            {
                cell.setCellType(CellType.NUMERIC);
                int value = (int) field.get(t);
                cell.setCellValue(value);
            }
            else if (type == Double.class)
            {
                cell.setCellType(CellType.NUMERIC);
                double value = (double) field.get(t);
                cell.setCellValue(value);
            }
            else if (type == BigInteger.class)
            {
                cell.setCellType(CellType.NUMERIC);
                BigInteger value = (BigInteger) field.get(t);
                cell.setCellValue(value.doubleValue());
            }
            else if (type == Long.class)
            {
                cell.setCellType(CellType.NUMERIC);
                long value = (long) field.get(t);
                cell.setCellValue(value);
            }
            else if (type == Boolean.class)
            {
                cell.setCellType(CellType.NUMERIC);
                boolean value = (boolean) field.get(t);
                cell.setCellValue(value);
            }
            else if (type == Date.class)
            {
                Date value = (Date) field.get(t);
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                if (value != null && annotation != null && StringUtils.isNotBlank(annotation.dateFormat()))
                {
                    try
                    {
                        cell.setCellType(CellType.STRING);
                        DateTime dateTime = new DateTime(value);
                        String dateString = dateTime.toString(annotation.dateFormat());
                        cell.setCellValue(dateString);
                    }
                    catch (Exception e1)
                    {
                        //swallow
                    }
                }
            }
            else if (type.isEnum())
            {
                Enum anEnum = (Enum) field.get(t);
                if (anEnum != null)
                {
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(anEnum.name());
                }
            }
            else
            {
                cell.setCellType(CellType.STRING);
                String value = (String) field.get(t);
                cell.setCellValue(value);
            }
        }
        catch (Exception e)
        {
            //swallow
        }
        AccessibleObject.setAccessible(new AccessibleObject[] {field}, accessible);
    }

    public static void validate(Excel excelAnnotation) throws Exception
    {
        if(StringUtils.isBlank(excelAnnotation.sheetName()))
        {
            throw new Exception("sheetName can not be empty");
        }
        if(excelAnnotation.firstRow() < 1)
        {
            throw new Exception("firstRow can not be less than 1");
        }
    }
}
