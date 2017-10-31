package eCare.model.services;

import eCare.model.DAO.BlockingFeaturesDAO;
import eCare.model.PO.BlockingFeatures;
import jdk.nashorn.internal.ir.Block;

import java.util.List;

/**
 * Created by echerkas on 29.10.2017.
 */
public class BlockingFeaturesService {

    private static BlockingFeaturesDAO blockingFeaturesDAO;

    public BlockingFeaturesService() {
        blockingFeaturesDAO = new BlockingFeaturesDAO();
    }

    public void persist(BlockingFeatures entity) {
        blockingFeaturesDAO.openCurrentSessionwithTransaction();
        blockingFeaturesDAO.persist(entity);
        blockingFeaturesDAO.closeCurrentSessionwithTransaction();
    }

    public void update(BlockingFeatures entity) {
        blockingFeaturesDAO.openCurrentSessionwithTransaction();
        blockingFeaturesDAO.update(entity);
        blockingFeaturesDAO.closeCurrentSessionwithTransaction();
    }

    public BlockingFeatures findById(Integer id) {
        blockingFeaturesDAO.openCurrentSession();
        BlockingFeatures blockingFeatures = blockingFeaturesDAO.findById(id);
        blockingFeaturesDAO.closeCurrentSession();
        return blockingFeatures;
    }

    public void delete(Integer id) {
        blockingFeaturesDAO.openCurrentSessionwithTransaction();
        BlockingFeatures blockingFeatures = blockingFeaturesDAO.findById(id);
        blockingFeaturesDAO.delete(blockingFeatures);
        blockingFeaturesDAO.closeCurrentSessionwithTransaction();
    }

    public List<BlockingFeatures> findAll() {
        blockingFeaturesDAO.openCurrentSession();
        List<BlockingFeatures> blockingFeaturesList = blockingFeaturesDAO.findAll();
        blockingFeaturesDAO.closeCurrentSession();
        return blockingFeaturesList;
    }

    public void deleteAll() {
        blockingFeaturesDAO.openCurrentSessionwithTransaction();
        blockingFeaturesDAO.deleteAll();
        blockingFeaturesDAO.closeCurrentSessionwithTransaction();
    }

    public BlockingFeaturesDAO blockingFeaturesDAO() {
        return blockingFeaturesDAO;
    }
}
