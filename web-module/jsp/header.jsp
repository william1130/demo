<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--
<meta http-equiv="Content-Language" content="zh-TW">
-->
<meta http-equiv="Context-Script-Type" content="text/javascript">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="-1">
<meta http-equiv="cache-control" content="no-cache">
<link href='<c:url value="/css/common.css"/>' rel="stylesheet" type="text/css">
<link href='<c:url value="/css/pane.css"/>' rel="stylesheet" type="text/css">
<link href='<c:url value="/css/folding.css"/>' rel='stylesheet' type='text/css'>
<link href='<c:url value="/css/project.css"/>' rel='stylesheet' type='text/css'>
<link href='<c:url value="/css/widget.css"/>' rel='stylesheet' type='text/css'>
<script type="text/javascript" src="<c:url value='/scripts/jquery-3.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>" charset="UTF-8"></script>
<%-- <c:import url="/scripts/js_message.jsp"/>--%>
<%@include file="/scripts/js_message.jsp"%>

<script type="text/javascript" src="<c:url value='/scripts/fastinit.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/tablekit.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/common.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/project.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/widget.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/ajax_function.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/folding.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/validate.js'/>"></script>
<%--
<script type="text/javascript" src="<c:url value='/scripts/firebug/firebug.js'/>"></script>
--%>
<script type="text/javascript" src="<c:url value='/scripts/rico/rico.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/rico/ricoCommon.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/rico/ricoStyles.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/scriptaculous/scriptaculous.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/scriptaculous/effects.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/scriptaculous/builder.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/prado/prado.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/prado/scriptaculous-adapter.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/prado/controls/controls.js'/>" charset="UTF-8"></script>
<script type="text/javascript" src="<c:url value='/scripts/prado/datepicker/datepicker.js'/>" charset="UTF-8"></script>
<link href='<c:url value="/css/prado/prado.css"/>' rel='stylesheet' type='text/css' >
<link href='<c:url value="/scripts/prado/datepicker/default.css"/>' rel='stylesheet' type='text/css' >
<script type="text/javascript">
	var rootPath = "<c:url value="/" />";
	var timeoutId;
	function closeCurrentWindow()
	{
		if( timeoutId )
		{
			window.clearTimeout(timeoutId);
		}
		closeOpenerIFrameDialog();
	}

	EdsEvent.addOnLoad(
		function()
		{
			<c:if test="${param['autoClose']}">
				var timer = 3;
				<c:if test="${not empty param['timer']}">
					timer = <c:out value="${param['timer']}"/>;
				</c:if>
				$('autoCloseBlock').update("<p align='center'>本視窗將於 " + timer + " 秒後自動關閉.</p>");
				timeoutId = window.setTimeout( closeCurrentWindow, timer*1000 );
			</c:if>;
			<c:if test="${param['refreshOpener']}">
				BrowserUtils.reloadOpener();
			</c:if>;
		}
	);
</script>
<script>
if (window.NC) {
    NC.add('loader', function(S){
        var doc = document;
        var queue = [], readyBound = false;
        var testNode = doc.createElement('script'), fn, node;
        fn = testNode.readyState ? function(node, callback){
            node.onreadystatechange = function(){
                var rs = node.readyState;
                if (rs === 'loaded' || rs === 'complete') {
                    // handle memory leak in IE
                    node.onreadystatechange = null;
                    callback.call(this);
                }
            };
        }: function(node, callback){
		            node.onload = callback;
		};
        function dequeue(url, callback, charset){
            if ((url == undefined) || (url && url.type)) {
                var currentScript = queue.shift();
                if (currentScript) {
                    function plugins(){
                        currentScript.callback();
                        dequeue();
                    }
                    dequeue(currentScript.url, plugins, currentScript.charset);
                    
                }
            }
            else {
                var head = doc.getElementsByTagName('head')[0] || doc.documentElement, node = doc.createElement('script');
                node.src = url;
                if (charset) 
                    node.charset = charset;
                //node.async = true;
                if (NC.isFunction(callback)) {
                    fn(node, callback);
                }
                head.appendChild(node);
            }
        }
        function enqueue(url, callback, charset){
            queue.push({
                'url': url,
                'callback': callback,
                'charset': charset
            });
            domReady();
        }
        
        function domReady(){
            if (readyBound) {
                return;
            }
            
            readyBound = true;
            
            if (document.readyState === "complete") {
                return dequeue();
            }
            
            if (document.addEventListener) {
                document.addEventListener("DOMContentLoaded", dequeue, false);
                window.addEventListener("load", dequeue, false);
            }
            else 
                if (document.attachEvent) {
                    document.attachEvent("onreadystatechange", dequeue);
                    
                    window.attachEvent("onload", dequeue);
                    var toplevel = false;
                    try {
                        toplevel = window.frameElement == null;
                    } 
                    catch (e) {
                    }
                    
                    if (document.documentElement.doScroll && toplevel) {
                        try {
                            // If IE is used, use the trick by Diego Perini
                            // http://javascript.nwbox.com/IEContentLoaded/
                            document.documentElement.doScroll("left");
                        } 
                        catch (error) {
                            setTimeout(arguments.callee, 10);
                            return;
                        }
                        dequeue();
                    }
                }
        }
        
        S.loader = {
            getScript: function(url, callback, queue, charset){
                if (!queue) {
                    dequeue(url, callback, charset);
                }
                else {
                    enqueue(url, callback, charset)
                }
            },
            getCss: function(url, callback, charset){
                var head = document.getElementsByTagName('head').item(0);
                var node = document.createElement('link');
                node.href = url;
                node.type = 'text/css';
                node.rel = "stylesheet";
                if (NC.isFunction(callback)) {
                    fn(node, callback);
                }
                head.appendChild(node);
            },
            _globalEval: function(){
            
            }
        };
        
    })
}
</script>