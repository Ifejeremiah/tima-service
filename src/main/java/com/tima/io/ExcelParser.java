package com.tima.io;

import com.tima.model.ExcelRow;
import lombok.var;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.List;

public class ExcelParser {

    public List<ExcelRow> read(InputStream inputStream) throws Exception {
        ThreeCellSheetHandler handler = new ThreeCellSheetHandler();

        try (OPCPackage pkg = OPCPackage.open(inputStream)) {
            XSSFReader reader = new XSSFReader(pkg);
            DataFormatter formatter = new DataFormatter();

            var sheets = reader.getSheetsData();

            while (sheets.hasNext()) {
                try (InputStream sheet = sheets.next()) {
                    XMLReader parser = XMLReaderFactory.createXMLReader();
                    parser.setContentHandler(new XSSFSheetXMLHandler(reader.getStylesTable(), null, reader.getSharedStringsTable(), handler, formatter, false));
                    parser.parse(new InputSource(sheet));
                }
            }
        }

        return handler.getRows();
    }
}

