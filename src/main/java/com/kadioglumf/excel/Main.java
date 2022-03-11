package com.kadioglumf.excel;

import com.kadioglumf.excel.model.BaseExcel;
import com.kadioglumf.excel.model.Order;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Order> orders = new ArrayList<>();
        for (int i = 0 ; i< 5; i++) {
            orders.add(new Order("fatih_" + i, "kadioglu_" + i, "phone", i));
        }
        generateExcelAttachment(orders, Order.class, "asd");
    }

    public static <T extends BaseExcel> List<T> generateExcelAttachment(List<T> list, Class<T> clazz, String fileName)
    {
        List<T> customAttachments = new ArrayList<>();
        try
        {
            XSSFWorkbook xssfWorkbook = new ExcelWriter().writeOnWorkbook(list, clazz);
            byte[] decodedBytes = getWorkbookAsByteArray(xssfWorkbook);
            /*
            CustomAttachment attachment = new CustomAttachment();
            attachment.setDocumentContent(decodedBytes);
            attachment.setFileName(fileName + GeneralConstants.EXCEL_EXTESION);
            customAttachments.add(attachment);

             */
        }
        catch (Exception exception)
        {
            //logger.error("", exception);
        }
        return customAttachments;
    }


    private static byte[] getWorkbookAsByteArray(XSSFWorkbook workbook) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            workbook.write(bos);
        }
        catch (IOException e)
        {
            //logger.error("Hata oluştu: {}", e.getMessage());
        }
        finally
        {
            try
            {
                bos.close();
            }
            catch (IOException ex)
            {
                //logger.error("Connection Kapatılırken Hata oluştu: {}", ex.getMessage());
            }
        }
        return bos.toByteArray();
    }
}
