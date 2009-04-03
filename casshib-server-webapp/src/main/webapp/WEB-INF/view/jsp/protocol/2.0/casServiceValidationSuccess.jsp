<%@ page session="false" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>
	<cas:authenticationSuccess>
		<cas:user>${fn:escapeXml(assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.id)}</cas:user>
<%-- this adds <cas:attributes>...</cas:attributes> to the validation
     response if there are attributes available in the principal.  This is
     not part of the official CAS2 protocol specification, but this format
     is consistent with what the JA-SIG Java client expects (i.e., no
     modifications to the JA-SIG Java client are necessary, but
     modifications to other clients may be). --%>
<c:if test="${not empty assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.attributes}">
			<cas:attributes>
				<c:forEach var="attr"
					   items="${assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.attributes}"
					   varStatus="loopStatus" begin="0"
					   end="${fn:length(assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.attributes)-1}"
					   step="1">
					<cas:${fn:escapeXml(attr.key)}>${fn:escapeXml(attr.value)}</cas:${fn:escapeXml(attr.key)}>
				</c:forEach>
			</cas:attributes>
</c:if>
<c:if test="${not empty pgtIou}">
		<cas:proxyGrantingTicket>${pgtIou}</cas:proxyGrantingTicket>
</c:if>
<c:if test="${fn:length(assertion.chainedAuthentications) > 1}">
		<cas:proxies>
<c:forEach var="proxy" items="${assertion.chainedAuthentications}" varStatus="loopStatus" begin="0" end="${fn:length(assertion.chainedAuthentications)-2}" step="1">
			<cas:proxy>${fn:escapeXml(proxy.principal.id)}</cas:proxy>
</c:forEach>
		</cas:proxies>
</c:if>
	</cas:authenticationSuccess>
</cas:serviceResponse>