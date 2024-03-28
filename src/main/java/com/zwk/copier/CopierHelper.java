package com.zwk.copier;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/28 11:03
 */

public class CopierHelper {
    private CopierFactory copierFactory;

    public CopierHelper(CopierFactory copierFactory) {
        this.copierFactory = copierFactory;
    }


    public <L> L copy(L left, Object right) throws Exception {
        Copier copier = copierFactory.createCopier(left.getClass(), right.getClass());
        copier.copy(left, right);
        return left;
    }

    public <L> L copy(Class<L> left, Object right) throws Exception {
        Object l;
        if (Map.class.isAssignableFrom(left)) {
            l = new HashMap<>();
        } else {
            try {
                l = left.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Copier copier = copierFactory.createCopier(left, right.getClass());
        copier.copy(l, right);
        return (L) l;
    }

    public <L> L copy(String id, L left, Object right) throws Exception {
        Copier copier = copierFactory.createCopier(id, left.getClass(), right.getClass());
        copier.copy(left, right);
        return left;
    }

    public <L> L copy(String id, Class<L> left, Object right) throws Exception {
        Object l;
        if (Map.class.isAssignableFrom(left)) {
            l = new HashMap<>();
        } else {
            try {
                l = left.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Copier copier = copierFactory.createCopier(id, left, right.getClass());
        copier.copy(l, right);
        return (L) l;
    }
}
