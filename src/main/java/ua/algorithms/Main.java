package ua.algorithms;

import lombok.SneakyThrows;
import ua.algorithms.accessor.FileAccessor;
import ua.algorithms.accessor.GlobalFileAccessor;
import ua.algorithms.accessor.IndexFileAccessor;
import ua.algorithms.mvc.Controller;
import ua.algorithms.mvc.Model;
import ua.algorithms.mvc.controller.SimpleController;
import ua.algorithms.mvc.model.SimpleRepository;
import ua.algorithms.mvc.view.SimpleGUI;


public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        IndexFileAccessor indexFileAccessor =
                (IndexFileAccessor) FileAccessor.of("src/main/resources/index.bin", "INDEX");
        GlobalFileAccessor globalFileAccessor =
                (GlobalFileAccessor) FileAccessor.of("src/main/resources/global.bin", "GLOBAL");

        Model model = new SimpleRepository(indexFileAccessor, globalFileAccessor);
        Controller controller = new SimpleController(model);
        SimpleGUI simpleGUI = new SimpleGUI(controller);
        simpleGUI.init();
    }
}
