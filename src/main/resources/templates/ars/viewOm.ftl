<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>Object Model</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
            font-size: 8pt;
        }
        table, th, td {
            border: 1px solid black;
        }
        th {
            background-color: #c1e2b3;
            font-weight: bold;
        }
        th, td {
            height: 18px;
            line-height: 15px;
            padding: 0px 5px 0px 5px;
            text-align: left;
        }
        td {
            white-space: nowrap;
        }
    </style>
</head>
<body>

<div class="container-fluid">
    <#include "/inc/nav.ftl" />

    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-12">
            <#if spec??>
            <div><h3>Object Model of ${spec.neType} ${spec.neVersion}</h3></div>
            <div style="overflow: auto">
            <table>
                <thead>
                <tr>
                    <th>Topology</th>
                    <th>Alarming</th>
                    <th>Measured</th>
                    <!--
                    <th style="width:20px;">Configured</th>
                    <th style="width:20px;">Icon</th>
                    <th style="width:20px;">GUI launch</th>
                    <th style="width:60px;">3GPP NRM object</th>
                    <th style="width:30px;">Integration Version</th>
                    <th style="width:40px;">Integration (NASDA) </th>
                    -->
                    <th>Min</th>
                    <th>Max</th>
                    <th>Avg</th>
                    <th>Avg Per Network</th>
                    <th>Max Per Network</th>
                    <th>Max Per NE</th>
                    <th>Max NE</th>
                    <th>Avg NE</th>
                    <th>Max Per Root</th>
                    <th>Supported Releases</th>
                    <th>Transient/MO</th>
                    <th>Presentation</th>
                    <th>Name In OMeS</th>
                    <th>Adaptation ID</th>
                </tr>
                </thead>
                <tbody>
                <#list neType.adaptSet as adap>
                    <#list spec.ociMap[adap] as oci>
                    <tr>
                        <td style="border-bottom: 1px solid lightgray">
                            <#list 0..oci.column as i>
                                <#if i == oci.column>
                                    <span>|- ${oci.name}</span>
                                <#else>
                                    <span>|&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                </#if>
                            </#list>
                        </td>
                        <td>
                            <#if oci.alarmingObject == true>
                                A
                            </#if>
                        </td>
                        <td>
                            <#if oci.measuredObject == true>
                                M
                            </#if>
                        </td>
                        <!--
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        -->
                        <td>${oci.min?c}</td>
                        <td>${oci.max?c}</td>
                        <td>${oci.avg?c}</td>
                        <td>${oci.avgPerNet?c}</td>
                        <td>${oci.maxPerNet?c}</td>
                        <td>${oci.maxPerNE?c}</td>
                        <td>${oci.maxNePerNet?c}</td>
                        <td>${oci.avgNePerNet?c}</td>
                        <td>${oci.maxPerRoot}</td>
                        <td>${oci.supportedReleases}</td>
                        <td>
                            <#if oci.transient == true>
                                Transient
                            <#else>
                                MO
                            </#if>
                        </td>
                        <td>${oci.presentation}</td>
                        <td>${oci.nameInOmes}</td>
                        <td>${oci.adaptationId}</td>
                    </tr>
                    </#list>
                </#list>
                </tbody>
            </table>
                </div>
            </#if>
        </div>
        <div class="col-md-1"></div>
    </div>

    <#include "/inc/footer.ftl" />
</div>

<script src="/js/jquery-1.9.1.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script src="/js/ars.js"></script>
</body>
</html>