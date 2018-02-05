<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>PM Data Load</title>
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
            <div><h3>PM Data Load of ${spec.neType} ${spec.neVersion}</h3></div>
            <#list neType.adaptSet as adap>
                <div><h3>Adaptation ID: ${adap}</h3></div>
                <div style="overflow: auto;">
                <table>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Name In Omes</th>
                        <th>Measured Object</th>
                        <th>Is supported by ${spec.neVersion}</th>
                        <th>Supported other versions</th>
                        <th>Dimension</th>
                        <th>Avg Per Network</th>
                        <th>Max Per Network</th>
                        <th>Max Per NE</th>
                        <th>Nbr of Counters</th>
                        <th>N-1 Nbr of Counters</th>
                        <th>Delta</th>
                        <th>NW Agg Object</th>
                        <th>Time Agg</th>
                        <th>BH</th>
                        <th>Active</th>
                        <th>Default Interval</th>
                        <th>Minimum Interval</th>
                        <th>Storage</th>
                        <th>Bytes of counter</th>
                        <th>1 NE Measurements/h</th>
                        <th>1 NE Counters/h</th>
                        <th>1 NE hourly aggregation Counter/h</th>
                        <th>1 NE daily aggregation Counter/day</th>
                        <th>Max Measurements/h</th>
                        <th>Max Counters/h</th>
                        <th>Measurement Grouping Name</th>
                        <th>1 NE Raw rows in DB</th>
                        <th>1 NE Raw counters in DB</th>
                        <th>1 NE Space that will use for one measurement (Raw Data)</th>
                        <th>Max Raw rows in DB</th>
                        <th>Max Raw counters in DB</th>
                        <th>Max Space that will use for one measurement (Raw Data)</th>
                        <th>Total bytes per interval</th>
                        <th>Total Size per hour(GB)</th>
                        <th>Table Size per day (GB)</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list spec.measurementMap[adap] as meas>
                        <tr>
                            <td>
                                ${meas.name}
                            </td>
                            <td>
                                ${meas.nameInOmes}
                            </td>
                            <td>
                                ${meas.measuredObject}
                            </td>
                            <td>
                                ${meas.supported?string('Yes', 'No')}
                            </td>
                            <td>
                                ${meas.supportedOtherReleases}
                            </td>
                            <td>
                                ${meas.dimension}
                            </td>
                            <td>
                                ${meas.avgPerNet?c}
                            </td>
                            <td>
                                ${meas.maxPerNet?c}
                            </td>
                            <td>
                                ${meas.maxPerNe?c}
                            </td>
                            <td>
                                ${meas.counterNumber?c}
                            </td>
                            <td>
                                ${meas.counterNumberOfLastVersion?c}
                            </td>
                            <td>
                                ${meas.delta?c}
                            </td>
                            <td>
                                ${meas.aggObject}
                            </td>
                            <td>
                            ${meas.timeAgg!""}
                            </td>
                            <td>
                            ${meas.bh!""}
                            </td>
                            <td>
                                ${meas.active}
                            </td>
                            <td>
                                ${meas.defaultInterval?c}
                            </td>
                            <td>
                                ${meas.minimalInterval?c}
                            </td>
                            <td>
                                ${meas.storageDays?c}
                            </td>
                            <td>
                                ${meas.bytesPerCounter?c}
                            </td>
                            <td>
                                ${meas.mphPerNE?c}
                            </td>
                            <td>
                                ${meas.cphPerNE?c}
                            </td>
                            <td>
                                ${meas.chaPerNE?c}
                            </td>
                            <td>
                                ${meas.cdaPerNe?c}
                            </td>

                            <td>
                            ${meas.maxMph?c}
                            </td>
                            <td>
                            ${meas.maxCph?c}
                            </td>
                            <td>
                            ${meas.measGroup!""}
                            </td>
                            <td>
                            ${meas.dbRrPerNe?c}
                            </td>
                            <td>
                            ${meas.dbRcPerNe?c}
                            </td>
                            <td>
                            ${meas.msPerNe?c}
                            </td>
                            <td>
                            ${meas.dbMaxRows?c}
                            </td>
                            <td>
                            ${meas.dbMaxCtrs?c}
                            </td>
                            <td>
                            ${meas.maxMs?c}
                            </td>
                            <td>
                            ${meas.totalBytesPerInterval?c}
                            </td>
                            <td>
                            ${meas.totalSizePerHour?string["0.#########"]}
                            </td>
                            <td>
                            ${meas.tableSizePerDay?string["0.#########"]}
                            </td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
                </div>
            </#list>
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