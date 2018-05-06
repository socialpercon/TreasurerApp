package com.sora.treasurer.server;

import android.content.Context;

import com.android.volley.VolleyError;
import com.sora.treasurer.TAppEnums.RequestType;
import com.sora.treasurer.database.AppDatabase;
import com.sora.treasurer.database.entities.ExpenseEntity;
import com.sora.treasurer.http.GatewayListener;
import com.sora.treasurer.http.TRGateway;
import com.sora.treasurer.http.pojo.ExpenseEntityResponse;
import com.sora.treasurer.http.pojo.GatewayResponse;
import com.sora.treasurer.interfaces.RequestCallback;

import java.util.List;

/**
 * Created by Sora on 05/05/2018.
 */

public class TRServer {
    private static TRGateway mGateway;
    public static void ServerSync(final Context context, final RequestCallback requestCallback) {
        mGateway = new TRGateway(context,null);
        List<ExpenseEntity> expenseEntitiesWithoutServerID = AppDatabase.getDatabase(context).expenseDao().findEverything();
        ServerCreateExpenseBulk(expenseEntitiesWithoutServerID, context, new RequestCallback() {
            @Override
            public void onResponse(Object response) {
                ExpenseEntity[] expenseEntities = ((ExpenseEntity[]) response);

                for (ExpenseEntity ServerExpenseEntity : expenseEntities) {
                    ExpenseEntity expenseEntity = AppDatabase.getDatabase(context).expenseDao().findEntity(ServerExpenseEntity.getDateCreated(), ServerExpenseEntity.getExpenseValue(), ServerExpenseEntity.getExpenseType());
                    if (expenseEntity == null) {
                        ExpenseEntity SyncExpenseEntity = new ExpenseEntity(ServerExpenseEntity);
                        AppDatabase.getDatabase(context).expenseDao().CreateExpense(SyncExpenseEntity);
                    } else {
                        expenseEntity.UpdateEntity(ServerExpenseEntity);
                        AppDatabase.getDatabase(context).expenseDao().UpdateExpense(expenseEntity);
                    }
                }
                AfterSyncCleanUp(context);
                requestCallback.onResponse(response);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                requestCallback.onErrorResponse(error);
            }
        });
    }

    private static void ServerCreateExpenseBulk(List<ExpenseEntity> expenseEntities, final Context context,  final RequestCallback requestCallback) {
        mGateway = new TRGateway(context,null);
        mGateway.setResponseListener(new GatewayListener() {
            @Override
            public void onResponse(GatewayResponse gatewayResponse) {
                requestCallback.onResponse(((ExpenseEntityResponse) gatewayResponse).getData());
            }

            @Override
            public void onErrorResponse(VolleyError error, RequestType requestType) {
                requestCallback.onErrorResponse(error);
            }
        }).expenseAPICreate(expenseEntities);
    }

    private static void AfterSyncCleanUp(Context context) {
        mGateway = new TRGateway(context,null);
        List<ExpenseEntity> expenseEntities = AppDatabase.getDatabase(context).expenseDao().findEverything();
        for (ExpenseEntity expenseEntity : expenseEntities) {
            if (!expenseEntity.getActive()) {
                AppDatabase.getDatabase(context).expenseDao().deleteByID(expenseEntity.getExpenseID());
            }
        }
    }
}
