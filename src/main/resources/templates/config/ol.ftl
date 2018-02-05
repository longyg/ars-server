<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>Object Load</title>
</head>
<body>

<div class="container-fluid">
    <#include "/inc/nav.ftl" />

    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
            <div><h3>Object Load</h3></div>
            <div style="margin:10px">
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createOlFromModal">
                    <span class="glyphicon glyphicon-plus-sign"></span> Add
                </button>
            </div>
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
                <tbody>
                    <#list olList as ol>
                    <tr>
                        <td>${ol.objectClass}</td>
                        <td>${ol.max}</td>
                        <td>${ol.avg}</td>
                        <td>${ol.relatedObjectClass}</td>
                        <td>
                            <a class="btn btn-sm btn-info" href="/ol/edit?id=${ol.id}">
                                <span class="glyphicon glyphicon-edit"></span> Edit
                            </a>
                            <a class="btn btn-sm btn-danger" href="/ol/delete?id=${ol.id}">
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

<div class="modal fade" id="createOlFromModal" tabindex="-1" role="dialog"
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

                <form class="form-horizontal" role="form" action="/ol/add" method="post">
                    <div class="form-group">
                        <label  class="col-sm-3 control-label" for="objectClass">Object Class</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="objectClass" name="objectClass" placeholder="Object Class"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="max" >Max</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="max" name="max" placeholder="1"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="avg" >Avg</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="avg" name="avg" placeholder="1"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="relatedObjectClass" >Related Object Class</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="relatedObjectClass" name="relatedObjectClass" placeholder="Related Object Class" />
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
</body>
</html>