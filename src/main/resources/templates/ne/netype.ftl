<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>Network Element Type</title>
</head>
<body>

<div class="container-fluid">
    <#include "/inc/nav.ftl" />

    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
            <div><h3>Network Elements Type</h3></div>
            <div style="margin:10px">
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createNETypeFromModal">
                    <span class="glyphicon glyphicon-plus-sign"></span> Add
                </button>
            </div>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Type Name</th>
                    <th>Presentation</th>
                    <th>Agent Class</th>
                    <th>Description</th>
                    <th>Adaptations</th>
                    <th>Operations</th>
                </tr>
                </thead>
                <tbody>
                    <#list neTypeList as neType>
                    <tr>
                        <td>${neType.name}</td>
                        <td>${neType.presentation}</td>
                        <td>${neType.agentClass}</td>
                        <td>${neType.description}</td>
                        <td>
                            <#list neType.adaptSet as adap>
                                <ul>
                                    <li>${adap}</li>
                                </ul>
                            </#list>
                        </td>
                        <td>
                            <a class="btn btn-sm btn-info" href="/netype/edit?id=${neType.id}">
                                <span class="glyphicon glyphicon-edit"></span> Edit
                            </a>
                            <a class="btn btn-sm btn-danger" href="/netype/delete?id=${neType.id}">
                                <span class="glyphicon glyphicon-remove-sign"></span> Delete
                            </a>
                        </td>
                    </tr>
                    </#list>
                </tbody>
            </table>
        </div>
        <div class="col-md-1"></div>
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
                    Add Network Element Type
                </h4>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">

                <form class="form-horizontal" role="form" action="/netype/add" method="post">
                    <div class="form-group">
                        <label  class="col-sm-3 control-label" for="name">Type Name</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="name" name="name" placeholder="NE Type Name"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="presentation" >Presentation</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="presentation" name="presentation" placeholder="Presentation"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="agentClass" >Agent Class</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="agentClass" name="agentClass" placeholder="Agent Class" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="description" >Description</label>
                        <div class="col-sm-8">
                            <textarea class="form-control" id="description" name="description" placeholder="Description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="adaptations" >Adaptations</label>
                        <div class="col-sm-9" id="adaptDiv">
                            <div class="row form-group">
                                <div class="col-sm-9">
                                    <input class="form-control" name="adaptations" placeholder="Adaptation ID" />
                                </div>
                                <div class="col-sm-3">
                                    <button id="adaptAddBtn" type="button" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span></button>
                                </div>
                            </div>
                        </div>
                    </div>

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
<script src="/js/netype.js"></script>
</body>
</html>