
package com.company.Application.Controllers;


import javax.xml.bind.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
/**
 * Singleton
 * uses to load and save collection to xml
 */
public class XMLController {
    private static XMLController xmlController;
    private String filepath;
    private BasicFileAttributes attrs ;
    private XMLController() {

    }

    /**
     * get file attributes
     * @return BasicFileAttributes
     */
    BasicFileAttributes getAttrs(){
        return  attrs;
    }

    /**
     * return instance of this class
     * @return XMLController
     */
    public static XMLController getInstance() {
        if (xmlController == null) {
            xmlController = new XMLController();
        }
        return xmlController;
    }

    /**
     * converts class TreeMapController (and Map products in it) to XML file
     */
    public void convertToXml() {


        try {
            StringWriter xml = new StringWriter();

            JAXBContext context = JAXBContext.newInstance(TreeMapController.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(TreeMapController.getInstance(), xml);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filepath));

            attrs = Files.readAttributes(Paths.get(filepath), BasicFileAttributes.class);

            writer.write(xml.toString());
            writer.close();

        } catch (JAXBException e) {
            System.out.println("Ошибка в документации коллекции");

        } catch (FileNotFoundException e){
            System.out.println("Ошибка при доступе к файлу на запись");
        } catch (IOException e) {
            System.out.println("Ошибка при записи сохранении");
        }


    }

    /**
     * converts XML file to Map of products
     * @param path String
     */
    public void loadTree(String path) {
        try {
            BufferedInputStream baits = new BufferedInputStream(new FileInputStream(path));
            attrs = Files.readAttributes(Paths.get(path),BasicFileAttributes.class);

            JAXBContext context = JAXBContext.newInstance(TreeMapController.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            TreeMapController o = (TreeMapController) unmarshaller.unmarshal(baits);
            TreeMapController.getInstance().setProducts(o.getProducts());

        }
        catch (JAXBException e){
            System.out.println("Возможно ошибка при загрузке коллекции\n" +
                    "Коллекция будет загружена пустой");

        }
        catch (FileNotFoundException e)
        {
            System.out.println("Файл не был найден, при сохранении будет создан новый");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении атрибутов файла");
        } finally {
            filepath = path;
        }

    }
}
