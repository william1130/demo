




function addToolTip(node, title, values, formDiv) {
	
	//	---------------------------------------------------------
	var getDocumentOffsetTop = function(obj) {
			return obj.offsetTop +
				(obj.offsetParent ? getDocumentOffsetTop(obj.offsetParent) : 0);
	}
	
	//	---------------------------------------------------------
	var getDocumentOffsetLeft = function(obj) {
			return obj.offsetLeft +
				(obj.offsetParent ? getDocumentOffsetLeft(obj.offsetParent) : 0);
	}
	
	//	---------------------------------------------------------
	var clearArray = function(arr) {
		arr.splice(0, arr.length);
	};
	
	//	---------------------------------------------------------
	var getToolTipDiv = function() {
	
		try {
			
			var div = document.getElementById("edsToolTipDiv");
			
			if (div == undefined) {
			
				div = document.createElement("div");
				div.id = "edsToolTipDiv";
				
				div.style.position = "absolute";
				div.style.left = "0px";
				div.style.top = "0px";
				div.style.width = "auto";
				div.style.height = "auto";
				div.style.zIndex = 999;
				div.style.visibility = "hidden";
				
				div.className = "edsToolTip";
				document.body.appendChild(div);
			
				div.onmouseout = hiddenToolTip;
				
				div.onmouseover = function() {
					
					if (div["timeout"] != undefined) {
						
						clearTimeout(div["timeout"]);
					}
							
					div["timeout"] = undefined;
				};
				
				var fd = document.createElement("fieldset");
				fd.style.overflow = "hidden";
				div.appendChild(fd);
				div["contentFieldset"] = fd;
				
				var leg = document.createElement("legend");
				fd.appendChild(leg);
				div["contentLegend"] = leg;
				
				var con = document.createElement("div");
				fd.appendChild(con);
				div["contentValueDiv"] = con;
				con.style.textAlign = "left"
				con.style.whiteSpace = "nowrap";
			}

			return div;
		}
		catch (e) {
			if (e.description)
				alert(e.description);
			else
				alert(e);
		}
		
	};

	//	---------------------------------------------------------
	var hiddenToolTip = function() {
	
		var div = getToolTipDiv();
				
		if (div["timeout"] != undefined) {
				
			clearTimeout(div["timeout"]);
		}
		
		div["timeout"] = setTimeout(
			function() {
			
				div.style.visibility = "hidden";
				div["timeout"] = undefined;
			
			}, 300);
	};
	
	
	//	------------------------------------------------
	function showToolTip(node) {
			
		var div = getToolTipDiv();
			
		var toShowToolTip = function() {
				
			try {
				
				div.style.visibility = "hidden";
				div.style.width = "auto";
				div.style.height = "auto";
				
				if (div["timeout"] != undefined) {
				
					clearTimeout(div["timeout"]);
				}
				
				div["timeout"] = undefined;
	
				var leg = div["contentLegend"];
				var fieldset = div["contentFieldset"];
				
				if (div["contentLegendText"] != undefined) {
					
					leg.removeChild(div["contentLegendText"]);
					div["contentLegendText"] = undefined;
				}
				
				leg.appendChild(node["toolTipTitle"]);
				div["contentLegendText"] = node["toolTipTitle"];
				
				if (div["contentList"] == undefined)
					div["contentList"] = new Array();
					
				var cDiv = div["contentValueDiv"];
				var cList = div["contentList"];
					
				for (var i = 0; i < cList.length; i++) {
				
					cDiv.removeChild(cList[i]);
				}
				
				clearArray(cList);
				
				for (var i = 0; i < node["toolTipValues"].length; i++) {
					
					cList.push(node["toolTipValues"][i]);
					cDiv.appendChild(node["toolTipValues"][i]);
				}
	
				div.style.visibility = "visible";
				
				var divWidth = cDiv.offsetWidth + 64;
				var divHeight = fieldset.offsetHeight + 16;
				//var divHeight = leg.offsetHeight + cDiv.offsetHeight + 24;
				
				div.style.width = divWidth;
				div.style.height = divHeight;
				
				var scrollTop = 
					document.documentElement.scrollTop ||
					document.body.scrollTop || 0;
				var scrollLeft = 
					document.documentElement.scrollLeft ||
					document.body.scrollLeft || 0;
				var innerHeight = 
					window.innerHeight ||
					document.documentElement.clientHeight ||
					document.body.clientHeight || 400;
				var innerWidth = 
					window.innerWidth ||
					document.documentElement.clientWidth ||
					document.body.clientWidth || 600;
					
				var halfHeight = innerHeight - divHeight - 16;
				var halfWidth = innerWidth - divWidth - 16;
				
				var documentOffsetTop = getDocumentOffsetTop(node);
				var documentOffsetLeft = getDocumentOffsetLeft(node);
				
				var visiTop = documentOffsetTop - scrollTop;
				var visiLeft = documentOffsetLeft - scrollLeft;
				
				var setTop = documentOffsetTop + node.offsetHeight;
				var setLeft = documentOffsetLeft;
				
				if (visiTop > halfHeight)
					setTop = documentOffsetTop - div.offsetHeight;
				if (visiLeft > halfWidth)
					setLeft = documentOffsetLeft - div.offsetWidth + node.offsetWidth;
					
				
				div.style.left = setLeft;
				div.style.top = setTop;
			}
			catch (e) {
				if (e.description)
					alert(e.description);
				else
					alert(e);
			}
		};
				
		if (div["timeout"] != undefined) {
				
			clearTimeout(div["timeout"]);
		}
		
		div["timeout"] = setTimeout(
			toShowToolTip, 300
		);
	}
	
	//	------------------------------------------------
	try {
			
		node.style.cursor = "pointer";
		
		node.onmouseover = function() {
			showToolTip(this);
		};
		
		node.onmouseout = hiddenToolTip;
		
		node["toolTipTitle"] = document.createTextNode(title);
	
		var toolTipValues;
		
		if (formDiv) {
			
			var tooltipElementMap = this["tooltipElementMap"];
			
			if (tooltipElementMap == undefined) {
			
				tooltipElementMap = {};
				this["tooltipElementMap"] = tooltipElementMap ;
			}
			
			toolTipValues = tooltipElementMap[values];
			
			if (toolTipValues == undefined) {
				
				toolTipValues = new Array();
				tooltipElementMap[values] = toolTipValues;
				var srcDiv = document.getElementById(values);
				var nList = srcDiv == undefined ? undefined : srcDiv.childNodes;
				
				if (nList != undefined) {
					
					for (var i = 0; i < nList.length; i++) {
						
						toolTipValues.push(nList[i]);
					}
					
					for (var i = 0; i < nList.length; i++) {
						
						srcDiv.removeChild(nList[i]);
					}
				}
			}
		}
		else {

			toolTipValues = new Array();
			var texts = values.split("\n");
				
			for (var i = 0; i < texts.length; i++) {
				
				if (i != 0) {
					
					var br = document.createElement("br");
					toolTipValues.push(br);
				}
					
				var nod = document.createTextNode(texts[i]);
				toolTipValues.push(nod);
			}
		}
			
		node["toolTipValues"] = toolTipValues;
		showToolTip(node);
		
	}
	catch (e) {
		
		if (e.description)
			alert(e.description);
		else
			alert(e);
	}
}