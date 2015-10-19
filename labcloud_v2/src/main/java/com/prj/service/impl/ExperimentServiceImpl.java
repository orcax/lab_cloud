package com.prj.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prj.dao.AccountDao;
import com.prj.dao.ClazzDao;
import com.prj.dao.ExperimentDao;
import com.prj.entity.Clazz;
import com.prj.entity.Experiment;
import com.prj.entity.ExperimentStatus;
import com.prj.service.ExperimentService;
import com.prj.util.DataWrapper;

@Service
public class ExperimentServiceImpl extends BaseServiceImpl<Experiment, Long> implements
                                                                            ExperimentService {
    ExperimentDao ed;

    @Autowired
    ClazzDao      clazzDao;

    @Autowired
    AccountDao    accountDao;

    @Autowired
    ClazzDao      cd;

    @Autowired
    public ExperimentServiceImpl(ExperimentDao ed) {
        super(Experiment.class, ed);
        this.ed = ed;
    }

    public DataWrapper create(Experiment entity) {
        assertUniqueNumber(entity.getNumber());
        return super.createEntity(entity);
    }

    public DataWrapper listInfoByLab(Long lab) {
        // TODO Auto-generated method stub
        return null;
    }

    public DataWrapper listInfoByCourse(Long course) {
        // TODO Auto-generated method stub
        return null;
    }

    public DataWrapper listByLab(Long lab) {
        // TODO Auto-generated method stub
        return null;
    }

    public DataWrapper addLab(long expId, long labId) {
        return ed.addLabExp(labId, expId);
    }

    public DataWrapper listByClazz(Long clazzId) {
        Clazz clazz = cd.read(clazzId);
        List<Experiment> experiments = new ArrayList<Experiment>(clazz.getCourse().getExperiments());
        return new DataWrapper(experiments);
    }

    public DataWrapper listInfoByClazz(Long courseId) {
        // TODO Auto-generated method stub
        return null;
    }

    public DataWrapper listLabs(Long expId) {

        return ed.listLabsByExp(expId);
    }

    public DataWrapper deleteLabExp(long expId, long labId) {
        return ed.deleteLabExp(labId, expId);
    }

    public DataWrapper statusListByClazz(long clazzId) {
        Clazz clazz = cd.read(clazzId);
        List<Experiment> experiments = new ArrayList<Experiment>(clazz.getCourse().getExperiments());
        List<Long> ids = new ArrayList<Long>();

        if (experiments.size() == 0) {
            return new DataWrapper(new ArrayList<ExperimentStatus>());
        }

        for (Experiment exp : experiments) {
            ids.add(exp.getId());
        }

        return  ed.statusListByExpIds(ids, clazz.getStudents().size(), clazzId);
    }

    public DataWrapper getStudentAvailableExperimentOfSermester(long accountId, long semesterId) {
        List<Clazz> clazzs = clazzDao.listSutdentSemesterClazz(semesterId, accountId);
        LinkedList<Experiment> list = new LinkedList<Experiment>();
        for (Clazz clz : clazzs) {
            for (Experiment exp : clz.getCourse().getExperiments()) {
                list.add(exp);
            }
        }
        return new DataWrapper(list);
    }

}
