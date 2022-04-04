package Helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractData {
    static String dataPath = System.getProperty("user.dir") + "/src/test/resources/Data.xlsx";
    static Logger logger = LogManager.getLogger(ExtractData.class.getName());

    public static User getSingleRandomUser() {
        XSSFWorkbook workbook;
        User user = null;
        try {
            workbook = new XSSFWorkbook(dataPath);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int totalEntries = sheet.getLastRowNum();
            int ind = (int) Math.floor(Math.random() * totalEntries);
            if (ind == 0) ind = 1;
            XSSFRow row = sheet.getRow(ind);
            user = new User(row.getCell(0).toString(), row.getCell(1).toString(),
                            row.getCell(2).toString(), (int) row.getCell(3).getNumericCellValue());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return user;
    }
    public static List<User> getAllUsers() {
        XSSFWorkbook workbook;
        List<User> users = new ArrayList<>();
        try {
            workbook = new XSSFWorkbook(dataPath);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                User user = new User(row.getCell(0).toString(), row.getCell(1).toString(),
                        row.getCell(2).toString(), (int) row.getCell(3).getNumericCellValue());
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return users;
    }

    public static List<String> getAllTasks() {
        XSSFWorkbook workbook;
        List<String> tasks = new ArrayList<>();
        try {
            workbook = new XSSFWorkbook(dataPath);
            XSSFSheet sheet = workbook.getSheetAt(1);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                tasks.add(row.getCell(0).getStringCellValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return tasks;
    }

    public static User getFirstOrLast(boolean isFirst) {
        XSSFWorkbook workbook;
        User user = null;
        try {
            workbook = new XSSFWorkbook(dataPath);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int totalEntries = sheet.getLastRowNum();
            int ind = isFirst? 1: sheet.getLastRowNum();
            XSSFRow row = sheet.getRow(ind);
            user = new User(row.getCell(0).toString(), row.getCell(1).toString(),
                    row.getCell(2).toString(), (int) row.getCell(3).getNumericCellValue());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return user;
    }
}
