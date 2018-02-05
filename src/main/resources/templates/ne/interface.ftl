<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>NE Interfaces</title>
</head>
<body>

<div class="container-fluid">
    <#include "/inc/nav.ftl" />

    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
            <div><h3>NE interfaces</h3></div>
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-1 control-label" for="neTypeSelect">NE Type</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="neTypeSelect" name="neType">
                            <option value="">--Select--</option>
                            <#list allNeTypeList as type>
                                <#if neTypeId?? && type.id == neTypeId>
                                    <option value="${type.id}" selected>${type.name}</option>
                                <#else>
                                    <option value="${type.id}">${type.name}</option>
                                </#if>
                            </#list>
                        </select>
                    </div>
                    <label class="col-sm-1 control-label" for="neVersionSelect">NE Version</label>
                    <div class="col-sm-2">
                        <select class="form-control" id="neVersionSelect" name="neVersion">
                            <option value="">--Select--</option>
                        <#list neReleaseList as neRel>
                            <#if neRelId?? && neRel.id == neRelId>
                                <option value="${neRel.id}" selected>${neRel.neVersion}</option>
                            <#else>
                                <option value="${neRel.id}">${neRel.neVersion}</option>
                            </#if>
                        </#list>
                        </select>
                    </div>
                    <#if neRelId?? && "" != neRelId && (selectableIntObjs?size > 0)>
                        <div class="col-sm-1">
                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createInterfaceFromModal">
                                <span class="glyphicon glyphicon-plus-sign"></span> Add
                            </button>
                        </div>
                    </#if>
                </div>
            </form>

            <label class="col-md-12"></label>

            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>NE Type</th>
                    <th>NE Version</th>
                    <th>Interface Object</th>
                    <th>Operations</th>
                </tr>
                </thead>
                <tbody>
                <#if neInterface??>
                    <#list neIntObjs as intObj>
                        <tr>
                            <td>${neInterface.neType}</td>
                            <td>${neInterface.neVersion}</td>
                            <td>${intObj.name}</td>
                            <td>
                                <a class="btn btn-sm btn-danger" href="/neif/delete?id=${intObj.id}&neTypeId=${neTypeId}&neRelId=${neRelId}">
                                    <span class="glyphicon glyphicon-remove-sign"></span> Delete
                                </a>
                            </td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
        <div class="col-md-1"></div>
    </div>

    <#include "/inc/footer.ftl" />
</div>

<div class="modal fade" id="createInterfaceFromModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    Add Interface
                </h4>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">

                <form class="form-horizontal" role="form" action="/neif/add" method="post">
                    <#if neTypeId??>
                        <input type="hidden" name="neTypeId" value="${neTypeId}" />
                    </#if>
                    <#if neRelId??>
                        <input type="hidden" name="neRelId" value="${neRelId}">
                    </#if>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="interface" >Interface</label>
                        <div class="col-sm-10">
                            <select class="form-control" name="interface" id="interface">
                            <#list selectableIntObjs as sIntObj>
                                <option value="${sIntObj.id}">${sIntObj.name}</option>
                            </#list>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="submit" class="btn btn-primary">Add</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>

<script src="/js/jquery-1.9.1.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script src="/js/neif.js"></script>
</body>
</html>