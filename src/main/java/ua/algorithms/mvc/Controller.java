package ua.algorithms.mvc;

public interface Controller {

    String select(String pk);
    String insert(String pk, String firstName, String lastName, String email);
    String update(String pk, String firstName, String lastName, String email);
    String delete(String pk);

}
