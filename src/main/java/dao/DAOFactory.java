package dao;

public class DAOFactory {
    public static CompraComidaDAO getExcursionDAO() {
        return new CompraComidaDAOImpl();
    }
}
