<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>ARS Config</title>
</head>
<body>

<div class="container-fluid">
    <#include "/inc/nav.ftl" />

    <div class="row">
        <div class="col-md-3"></div>
        <div class="col-md-6">
            <div><h3>Add ARS Config</h3></div>
            <form class="form-horizontal" role="form">
                <#if neTypeId??>
                <input type="hidden" name="neTypeId" value="${neTypeId}" />
                </#if>
                <#if neRelId??>
                <input type="hidden" name="neRelId" value="${neRelId}" />
                </#if>
                <div class="form-group">
                    <label  class="col-sm-3 control-label" for="neType">NE Type</label>
                    <div class="col-sm-9">
                        <input class="form-control" id="neType" name="neType" placeholder="${neRelease.neType}" value="${neRelease.neType}" readonly unselectable="on"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="neVersion" >NE Version</label>
                    <div class="col-sm-9">
                        <input class="form-control" id="neVersion" name="neVersion" placeholder="${neRelease.neVersion}" value="${neRelease.neVersion}" readonly unselectable="on"/>
                    </div>
                </div>
                <hr/>
                <h4><span class="glyphicon glyphicon-link"></span> Interfaces</h4>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="interfaceSelect" >Interfaces</label>
                    <div class="col-sm-9">
                        <div class="row form-group">
                            <div class="col-sm-5">
                                <select class="form-control" name="interface" id="interfaceSelect">
                                    <option value="">--Select--</option>
                                    <#list selectableInterfaces as iface>
                                        <option value="${iface.id}">${iface.name}</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="col-sm-3" id="ifaceAddBtnDiv">
                                <button id="ifaceAddBtn" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span></button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group" id="ifaceTable">
                    <label class="col-sm-3 control-label">Selected Interfaces</label>
                    <div class="col-sm-9">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Operations</th>
                            </tr>
                            </thead>
                            <tbody id="ifaceTableBody">
                            <#list supportedInterfaces as iface>
                            <tr>
                                <td>${iface.name}</td>
                                <td>Delete</td>
                            </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
                <hr/>
                <h4><span class="glyphicon glyphicon-link"></span> Resources</h4>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="adapIdSelect" >Adaptation ID</label>
                    <div class="col-sm-9">
                        <div class="row form-group">
                            <div class="col-sm-5">
                                <select class="form-control" name="adaptationId" id="adapIdSelect">
                                    <option value="">--Select--</option>
                                    <#if neType??>
                                        <#list neType.adaptSet as adapId>
                                            <option value="${adapId}">${adapId}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                            <div class="col-sm-3">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label" for="adapVersionSelect" >Adaptation Version</label>
                    <div class="col-sm-9">
                        <div class="row form-group">
                            <div class="col-sm-5">
                                <select class="form-control" name="adaptationRelease" id="adapVersionSelect">
                                    <option>--Select--</option>
                                </select>
                            </div>
                            <div class="col-sm-3" id="srcAddBtnDiv">
                                <button id="srcAddBtn" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span></button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group" id="srcTable">
                    <label class="col-sm-3 control-label">Selected Resources</label>
                    <div class="col-sm-9">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>Adaptation ID</th>
                                <th>Adaptation Version</th>
                                <th>Operations</th>
                            </tr>
                            </thead>
                            <tbody id="srcTableBody">
                            <#list supportedResources as src>
                            <tr>
                                <td>${src.adaptation.id}</td>
                                <td>${src.adaptation.release}</td>
                                <td>Delete</td>
                            </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>

                <hr/>
                <h4><span class="glyphicon glyphicon-link"></span> Parent Hierarchy</h4>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="parentAdapIdSelect" >Adaptation ID</label>
                    <div class="col-sm-9">
                        <div class="row form-group">
                            <div class="col-sm-5">
                                <select class="form-control" name="parentAdapId" id="parentAdapIdSelect">
                                    <option value="">--Select--</option>
                                <#if neType??>
                                    <#list neType.adaptSet as adapId>
                                        <option value="${adapId}">${adapId}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                            <div class="col-sm-3">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label" for="parent" >Parent Hierarchy</label>
                    <div class="col-sm-9">
                        <div class="row form-group">
                            <div class="col-sm-5">
                                <input class="form-control" name="parent" id="parent" />
                            </div>
                            <div class="col-sm-3" id="parentAddBtnDiv">
                                <button id="parentAddBtn" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span></button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group" id="parentTable">
                    <label class="col-sm-3 control-label">Parent Setting</label>
                    <div class="col-sm-9">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>Adaptation ID</th>
                                <th>Parent Hierarchy</th>
                                <th>Operations</th>
                            </tr>
                            </thead>
                            <tbody id="parentTableBody">
                            <#list supportedParents as adapId, parent>
                                <tr>
                                    <td>${adapId}</td>
                                    <td>${parent}</td>
                                    <td>Delete</td>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>

                <hr/>
                <h4><span class="glyphicon glyphicon-link"></span> Object Load</h4>
                <div class="form-group">
                    <label class="col-sm-3 control-label">Add Object Load</label>
                    <div class="col-sm-9">
                        <div class="row form-group">
                            <div class="col-sm-3" id="loadAddBtnDiv">
                                <button id="loadAddBtn" type="button" class="btn btn-primary" data-toggle="modal" data-target="#createNETypeFromModal"><span class="glyphicon glyphicon-plus"></span></button>
                    <label class="col-sm-3 control-label" for="loadSelect" >Object Load</label>
                    <div class="col-sm-9">
                        <div class="row form-group">
                            <div class="col-sm-6">
                                <select class="form-control" name="load" id="loadSelect">
                                    <option value="">--Select--</option>
                                <#list selectableLoads as load>
                                    <option value="${load.id}">
                                        ${load.objectClass}:${load.relatedObjectClass} [Max:${load.max}, Avg:${load.avg}]
                                    </option>
                                </#list>
                                </select>
                            </div>
                            <div class="col-sm-3" id="loadAddBtnDiv">
                                <button id="loadAddBtn" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span></button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group" id="parentTable">
                    <label class="col-sm-3 control-label">Object Load Setting</label>
                <div class="form-group">
                    <label class="col-sm-3 control-label">Selected Object Load</label>
                    <div class="col-sm-9">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>Object Class</th>
                                <th>Max</th>
                                <th>Avg</th>
                                <th>Related Object Class</th>
                                <th>Operations</th>
                            </tr>
                            </thead>
                            <tbody id="olTableBody">
                            <#list supportedLoads as load>
                            <tr>
                                <td>${load.objectClass}</td>
                                <td>${load.max}</td>
                                <td>${load.avg}</td>
                                <td>${load.relatedObjectClass}</td>
                                <td>Delete</td>
                            </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
            </form>
        </div>
        <div class="col-md-3"></div>
    </div>

    <#include "/inc/footer.ftl" />
</div>

<div class="modal fade" id="createNETypeFromModal" tabindex="-1" role="dialog"
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
                    Add Object Load
                </h4>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">

                <form class="form-horizontal" role="form" action="/netype/add" method="post">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th><input type="checkbox" id="selectAll" /></th>
                            <th>Object Class</th>
                            <th>Object Number</th>
                            <th>Related Object Class</th>
                        </tr>
                        </thead>
                        <tbody id="olTableBody">
                        </tbody>
                    </table>
                    <img src="/image/loading.gif">

                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-10">
                            <button type="submit" class="btn btn-primary">Save</button>
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
<script src="/js/addConfig.js"></script>
</body>
</html>