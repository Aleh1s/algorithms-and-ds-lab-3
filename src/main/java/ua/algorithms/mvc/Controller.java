package ua.algorithms.mvc;

public interface Controller {

    String findByPk(String pk);
    String insert(String pk, String value);
    String update(String pk, String newValue);
    String delete(String pk);

}
