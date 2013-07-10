package com.quickblox.snippets.modules;

import android.content.Context;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.result.Result;
import com.quickblox.internal.core.helper.StringifyArrayList;
import com.quickblox.internal.module.custom.request.QBCustomObjectRequestBuilder;
import com.quickblox.module.custom.QBCustomObjects;
import com.quickblox.module.custom.model.QBCustomObject;
import com.quickblox.module.custom.model.QBPermissions;
import com.quickblox.module.custom.model.QBPermissionsLevel;
import com.quickblox.module.custom.result.QBCustomObjectLimitedResult;
import com.quickblox.module.custom.result.QBCustomObjectPermissionResult;
import com.quickblox.module.custom.result.QBCustomObjectResult;
import com.quickblox.snippets.Snippet;
import com.quickblox.snippets.Snippets;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Oleg Soroka
 * Date: 11.10.12
 * Time: 12:46
 */
public class SnippetsCustomObjects extends Snippets {

    // Define custom object model in QB Admin Panel
    // http://image.quickblox.com/3f71573f1fd8b23a1e375b904a80.injoit.png
    String className = "SuperSample";
    String fieldHealth = "rating";
    String fieldPower = "text";

    public SnippetsCustomObjects(Context context) {
        super(context);

        snippets.add(createCustomObject);
        snippets.add(getCustomObjectById);
        snippets.add(deleteCustomObject);
        snippets.add(getCustomObjects);
        snippets.add(updateCustomObject);
        snippets.add(getGetCustomObjectsByIds);
        snippets.add(getCustomObjectsPermissions);

        snippets.add(getCustomsObjectWithFilters);
    }

    Snippet getCustomObjects = new Snippet("get objects") {
        @Override
        public void execute() {
            QBCustomObjects.getObjects(className, new QBCallbackImpl() {
                @Override
                public void onComplete(Result result) {
                    if (result.isSuccess()) {
                        QBCustomObjectLimitedResult coresult = (QBCustomObjectLimitedResult) result;

                        ArrayList<QBCustomObject> co = coresult.getCustomObjects();
                        System.out.println(">>> custom object list: " + co.toString());
                    } else {
                        handleErrors(result);
                    }
                }
            });
        }
    };

    Snippet createCustomObject = new Snippet("create object") {
        @Override
        public void execute() {

            className = "SuperSample";
            QBCustomObject customObject = new QBCustomObject(className);
//            customObject.put(fieldHealth, 99);
//            customObject.put(fieldPower, "android");
//            customObject.setParentId("50d9bf2d535c12344701c43a");
            QBPermissions qbPermissions = new QBPermissions();
            qbPermissions.setReadPermission(QBPermissionsLevel.OPEN);
            qbPermissions.setUpdatePermission(QBPermissionsLevel.OPEN);
            qbPermissions.setDeletePermission(QBPermissionsLevel.OPEN);
            customObject.setPermission(qbPermissions);

            QBCustomObjects.createObject(customObject, new QBCallbackImpl() {
                @Override
                public void onComplete(Result result) {

                    if (result.isSuccess()) {
                        QBCustomObjectResult customObjectResult = (QBCustomObjectResult) result;
                        QBCustomObject newCustomObject = customObjectResult.getCustomObject();

                        System.out.println(">>> custom object fields: " + newCustomObject.getFields().keySet().toString());
                    } else {
                        handleErrors(result);
                    }
                }
            });
        }
    };

    Snippet getGetCustomObjectsByIds = new Snippet("get custom objects by ids") {
        @Override
        public void execute() {

            StringifyArrayList<String> coIDs = new StringifyArrayList<String>();
            coIDs.add("51dd5d98efa357bc9d000006");
            coIDs.add("51dd4fa9efa3573864000051");

            QBCustomObjects.getObjectsByIds(className, coIDs, new QBCallbackImpl() {
                @Override
                public void onComplete(Result result) {
                    if (result.isSuccess()) {
                        QBCustomObjectLimitedResult taskResult = (QBCustomObjectLimitedResult) result;

                        System.out.format(">>> custom objects: " + taskResult.getCustomObjects().toString());
                    }
                }
            });
        }
    };

    Snippet getCustomObjectsPermissions = new Snippet("get custom object permission") {
        @Override
        public void execute() {
            String coId = "d2b65efa357cf59000029";
            QBCustomObjects.getObjectPermissions(className, coId, new QBCallbackImpl() {
                @Override
                public void onComplete(Result result) {
                    if (result.isSuccess()) {
                        QBCustomObjectPermissionResult qbCustomObjectPermissionResult = (QBCustomObjectPermissionResult) result;

                        System.out.format(">>> custom object read permission: " + qbCustomObjectPermissionResult.getPermissions().getReadLevel());
                    }
                }
            });
        }
    };

    Snippet getCustomsObjectWithFilters = new Snippet("get object with filters") {
        @Override
        public void execute() {
            String fieldName = "health";
            String fieldForSort = "integer_field";
            QBCustomObjectRequestBuilder requestBuilder = new QBCustomObjectRequestBuilder();
//            requestBuilder.sortAsc(fieldName);
//            requestBuilder.sortDesc(fieldName);

            // search records which contains exactly specified value
//            String fieldValue = "1";
//            requestBuilder.eq(fieldName, fieldValue);

            // Limit search results to N records. Useful for pagination. Maximum value - 100 (by default). If limit is equal to -1 only last record will be returned
//            requestBuilder.setPagesLimit(2);

            //Skip N records in search results. Useful for pagination. Default (if not specified): 0
            requestBuilder.setPagesSkip(4);

            // Search record with field which contains value according to specified value and operator
//            requestBuilder.lt("integer_field", 60);
//            requestBuilder.lte(fieldForSort, 1);
//            requestBuilder.gt(fieldForSort, 60);
//            requestBuilder.gte(fieldForSort, 99);
//            requestBuilder.ne(fieldForSort, 99);

            // for arrays
//            ArrayList<String> healthList = new ArrayList<String>();
//            healthList.add("man");
//            healthList.add("girl");
//            requestBuilder.in("tags", "man", "girl");
//            requestBuilder.nin("tags", healthList);
//            requestBuilder.count();

            QBCustomObjects.getObjects(className, requestBuilder, new QBCallbackImpl() {
                @Override
                public void onComplete(Result result) {
                    if (result.isSuccess()) {
                        QBCustomObjectLimitedResult coresult = (QBCustomObjectLimitedResult) result;
                        ArrayList<QBCustomObject> co = coresult.getCustomObjects();
                        System.out.println(">>> custom object list: " + co.toString());

                    } else {
                        handleErrors(result);
                    }

                    // if we use requestBuilder.count()
//                    QBCustomObjectCountResult countResult = (QBCustomObjectCountResult) result;
//                    Log.d("Count", String.valueOf(countResult.getCount()));
                }
            });
        }
    };

    Snippet getCustomObjectById = new Snippet("get object") {
        @Override
        public void execute() {
            QBCustomObject customObject = new QBCustomObject(className, "51d5a979efa357c7fa000006");

            QBCustomObjects.getObject(customObject, new QBCallbackImpl() {
                @Override
                public void onComplete(Result result) {
                    if (result.isSuccess()) {
                        QBCustomObjectResult customObjectResult = (QBCustomObjectResult) result;
                        QBCustomObject newCustomObject = customObjectResult.getCustomObject();

                        System.out.println(">>> custom object: " + newCustomObject);
                    } else {
                        handleErrors(result);
                    }
                }
            });
        }
    };

    Snippet deleteCustomObject = new Snippet("delete object") {
        @Override
        public void execute() {
            QBCustomObject customObject = new QBCustomObject(className, "51db14adefa3575e6400000a");

            QBCustomObjects.deleteObject(customObject, new QBCallbackImpl() {
                @Override
                public void onComplete(Result result) {
                    if (result.isSuccess()) {
                        System.out.println(">>> custom object deleted OK");
                    } else {
                        handleErrors(result);
                    }
                }
            });
        }
    };

    Snippet updateCustomObject = new Snippet("update object") {
        @Override
        public void execute() {
            QBCustomObject co = new QBCustomObject();
            co.setClassName(className);
            HashMap<String, Object> fields = new HashMap<String, Object>();
//            fields.put(fieldPower, 1);
//            fields.put(fieldHealth, "android nes");
//            co.setFields(fields);

            co.setCustomObjectId("51dd2b65efa357cf59000029");
            QBPermissions qbPermissions = new QBPermissions();
            qbPermissions.setReadPermission(QBPermissionsLevel.OPEN);
            qbPermissions.setUpdatePermission(QBPermissionsLevel.OPEN);
            qbPermissions.setDeletePermission(QBPermissionsLevel.OPEN);
            co.setPermission(qbPermissions);

            QBCustomObjects.updateObject(co, new QBCallbackImpl() {
                @Override
                public void onComplete(Result result) {
                    if (result.isSuccess()) {
                        QBCustomObjectResult updateResult = (QBCustomObjectResult) result;

                        System.out.println(">>> fields keys: " + updateResult.getCustomObject().getFields().keySet().toString());
                        System.out.println(">>> values keys: " + updateResult.getCustomObject().getFields().values().toString());

                        System.out.println(">>> co : " + updateResult.getCustomObject().toString());
                    } else {
                        handleErrors(result);
                    }
                }
            });
        }
    };
}