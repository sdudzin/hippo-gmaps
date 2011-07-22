<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.hippoecm.org/jsp/hst/core" prefix='hst'%>

<hst:headContribution>
    <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
</hst:headContribution>

<c:if test="${not empty document.title}">
  <hst:element var="headTitle" name="title">
    <c:out value="${document.title}"/>
  </hst:element>
  <hst:headContribution keyHint="headTitle" element="${headTitle}" />
</c:if>


<h2>${document.title}</h2>
<p>I18n key example: <fmt:message key="home.title"/></p>
<p>${document.summary}</p>
<hst:html hippohtml="${document.html}"/>

<h3>This document is geo-tagged with:</h3>
<p>
Longtitude: ${document.location.longitude}<br/>
Latitude: ${document.location.latitude}<br/>
Zoom: ${document.location.zoom}
</p>

Which is here:<br/>
<div id="map" style="width: 400px; height: 400px"></div>

<script type="text/javascript">
    var myLatlng = new google.maps.LatLng(${document.location.latitude}, ${document.location.longitude});
    var myOptions = {
        zoom: ${document.location.zoom},
        center: myLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(document.getElementById("map"), myOptions);
</script>


