package com.zero.library.mvp.databind;

import com.zero.library.mvp.model.IModel;
import com.zero.library.mvp.view.IDelegate;

public interface DataBinder<T extends IDelegate, D extends IModel> {

    void viewBindModel(T viewDelegate, D data);

}
