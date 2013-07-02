<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<openmrs:require privilege="Run Reports" otherwise="/login.htm" redirect="/module/amrsreports/queuedReport.form"/>

<openmrs:htmlInclude file="/dwr/util.js"/>
<openmrs:htmlInclude file="/dwr/interface/DWRAmrsReportService.js"/>
<openmrs:htmlInclude file="/moduleResources/amrsreports/js/jquery-ui-timepicker-addon.min.js"/>

<openmrs:htmlInclude file="/moduleResources/amrsreports/css/amrsreports.css" />

<style>
    .hidden { display: none; }
</style>

<script type="text/javascript">

    var reportDate;
    var dateScheduled;

    $j(document).ready(function () {
        reportDate = new DatePicker("<openmrs:datePattern/>", "reportDate", { defaultDate: new Date() });
        reportDate.setDate(new Date());

        var defaultOpts = {
            changeMonth: true,
            changeYear: true,
            ampm: true,
            controlType: 'select',
            buttonImageOnly: false,
            dateFormat: 'yy-mm-dd'
        };

        var elem = $j('#dateScheduled');
        elem.datetimepicker(defaultOpts);
        elem.datetimepicker('setDate', new Date());

        $j("#immediately").click(function(){
            if ($j("#immediately").is(":checked")) {
                $j("#dateScheduled").attr("disabled", "disabled");
            } else {
                $j("#dateScheduled").removeAttr("disabled");
            }
        });

        $j("#repeatSchedule").click(function(){
            if ($j("#repeatSchedule").is(":checked")) {
                $j("#repeatIntervalUnits").removeAttr("disabled");
                $j("#repeatInterval").removeAttr("disabled");
            } else {
                $j("#repeatInterval").val("");
                $j("#repeatIntervalUnits").attr("disabled", "disabled");
                $j("#repeatInterval").attr("disabled", "disabled");
            }
        });
    });

</script>
<style type="text/css">
    .ui-timepicker-div .ui-widget-header { margin-bottom: 8px; }
    .ui-timepicker-div dl { text-align: left; }
    .ui-timepicker-div dl dt { height: 25px; margin-bottom: -25px; }
    .ui-timepicker-div dl dd { margin: 0 10px 10px 65px; }
    .ui-timepicker-div td { font-size: 90%; }
    .ui-tpicker-grid-label { background: none; border: none; margin: 0; padding: 0; }

    .ui-timepicker-rtl{ direction: rtl; }
    .ui-timepicker-rtl dl { text-align: right; }
    .ui-timepicker-rtl dl dd { margin: 0 65px 10px 10px; }
</style>

<%@ include file="localHeader.jsp" %>

<b class="boxHeader">Add a Scheduled Report</b>

<div class="box" style=" width:99%; height:auto;  overflow-x: auto;">
    <form method="POST">
        <fieldset class="visualPadding">
            <legend>Dates</legend>
            <label for="reportDate">Report Date (as of):</label>
            <input type="text" name="reportDate" id="reportDate" size="20"/>
            <br /> <br />
            <label for="dateScheduled">Schedule Date:</label>
            <input type="text" id="dateScheduled" name="dateScheduled" size="20"/>
            <em>or</em>
                <input type="checkbox" name="immediate" id="immediate" value="true"/> Queue Immediately
              <br/><br/>
            <input type="checkbox" name="repeatSchedule" id="repeatSchedule" value="true"/> Check this for Repeat Schedule
            <br/>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label for="repeatInterval">Repeat Interval:</label>
            <input type="text" name="repeatInterval" id="repeatInterval" disabled="disabled"/>

            <select name="repeatIntervalUnits" id="repeatIntervalUnits" disabled="disabled">

                <option value="minutes">Minutes </option>
                <option value="hours">Hours </option>
                <option value="days" SELECTED="selected">Days </option>
            </select>


        </fieldset>
        <fieldset class="visualPadding">
            <legend>Location</legend>
            <select name="facility" id="facility"  size="10">
               <c:forEach var="facility" items="${facilities}">
                    <option value="${facility.facilityId}">${facility.code} - ${facility.name} </option>
               </c:forEach>
            </select>
        </fieldset>
        <fieldset class="visualPadding">
            <legend>Reports</legend>
            <c:forEach var="report" items="${reportProviders}">
                <div class="reportProvider<c:if test="${not report.visible}"> hidden</c:if>">
                    <input type="radio" name="reportName" value="${report.name}"/> ${report.name}
                </div>
            </c:forEach>
        </fieldset>
        <input id="submitButton" class="visualPadding newline" type="submit" value="Queue for processing"/>
    </form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>