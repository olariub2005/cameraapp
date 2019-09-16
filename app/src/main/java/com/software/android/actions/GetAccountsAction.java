package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Account;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetAccountsAction extends GetAction<List<Account>> {

    public GetAccountsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return getTarget() + "/" + GraphPath.ACCOUNTS;
    }

    @Override
    protected List<Account> processResponse(GraphResponse response) {
        Utils.DataResult<Account> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Account>>() {}.getType());
        return dataResult.data;
    }
}
