function getAttribute(e, attribute) {
	var att = e.style[attribute];
	if (att != "" && att != null)
		return att;
	
	if(e.id == "" && e.className == "")
		return "";
	for (var i=document.styleSheets.length - 1; i>=0; i--) {
		for (var j=document.styleSheets[i].cssRules.length - 1; j>=0; j--) {
			if (document.styleSheets[i].cssRules[j] == null) continue;
			var selector = String(document.styleSheets[i].cssRules[j].selectorText);
			if (isElement(selector, e)) {
				if ('style' in document.styleSheets[i].cssRules[j])
					att = document.styleSheets[i].cssRules[j].style[attribute];
				else
					att = "";
				if (att != "" && att != null)
					return att;
			}
		}
	}
	return "";
}

function isElement(selector, e) {
	if (selector == null)
		return false;
	if (e.id != "" && selectorContains(selector,'#'+e.id))
		return true;
	for(var i=0; i<e.classList.length; i++) {
		var className = e.classList.item(i);
		if (selectorContains(selector,"."+className)) {
			return true;
		}
	}
	return false;
}

function selectorContains(selector, s) {
	var str = String(selector);
	var pre = 0;
	while(true) {
		str = str.substr(pre,str.length);
		pre = str.indexOf(s);
		if (pre <= -1) 
			return false;
		//if (pre != 0 && str.chatAt(pre-1)!=',' && str.chatAt(pre-1)!=' ')
		//	continue;
		var i=s.length;
		while(pre+i <= str.length) {
			if(pre+i == str.length ||str.charAt(pre+i)==',')
				return true;
			if(str.charAt(pre+i)==' '){
				i++;
				continue;
			}
			break;
		}
		while(pre+i < str.length) {
			if(str.charAt(pre+i)!=',') {
				i++;
				continue;
			}
			i++;
			break;
		}
		pre = pre + i;
	}
	return false;
}

function parseLength(e, margin) {
	if (margin.length < 2)
		return 0;
	if (margin.substring(margin.length-1,margin.length) == '%') {
		var percentage = Number(margin.substring(0,margin.length-1))/100;
		return e.parentElement.offsetWidth*percentage;
	}
	if (margin.length > 2 && margin.substring(margin.length-2,margin.length) == 'px') {
		return Number(margin.substring(0,margin.length-2));
	}
	if (margin.length > 3 && margin.substring(margin.length-3, margin.length) == 'rem') {
		var factor = Number(margin.substring(0,margin.length-3));
		//TODO retrieve root element font size
		return 15*factor;
	}
	return 0;
}

function getElementLocation(e, attribute) {
	var att = e.style[attribute];
	if (att != "" && att != null)
		return "self";
	
	if(e.id == "" && e.className == "")
		return "";
	for (var i=document.styleSheets.length - 1; i>=0; i--) {
		for (var j=document.styleSheets[i].cssRules.length - 1; j>=0; j--) {
			if (document.styleSheets[i].cssRules[j] == null) continue;
			var selector = String(document.styleSheets[i].cssRules[j].selectorText);
			if ((selector != null) && (selector.indexOf(e.id) > -1 || selector.indexOf(e.className) > -1)) {
				if ('style' in document.styleSheets[i].cssRules[j])
					att = document.styleSheets[i].cssRules[j].style[attribute];
				else
					att = "";
				if (att != "" && att != null)
					return i+":"+j;
			}
		}
	}
	return "";
}

function scanElement(e, pAlign, x, y) {
	
	var out = [];
	var wasMoved = false;
	out[4] = "";
	var position = getAttribute(e,'position');
	out[4] = e.id + " | " + e.className;
	//out[4] = getElementLocation(e,'position');
	if (e.parentElement != null) {
		switch (position) {
			case '':
				break;
			case 'absolute':
				var parent = e.parentElement;
				while(getAttribute(parent,'position') == 'static') {
					parent = parent.parentElement;
				}
				if (parent != e.parentElement) {
					parent.appendChild(e.parentElement.removeChild(e));
					wasMoved = true;
				}
				e.style.position = 'absolute';
				break;
			case 'fixed':
				var body = e.ownerDocument.body;
				(body).appendChild(e.parentElement.removeChild(e));
				e.style.position = 'absolute';
				wasMoved = true;
				break;
		}
	}
	if (wasMoved)
		return {moved: true};
	//retrieving the objects total width and height:
	out[0] = e.offsetWidth;
	out[1] = e.offsetHeight;
	
	//retrieving the elements name
	//out[4] = e.id;
	//retrieving the objects left and top margin, if they exist
	var str = getAttribute(e,'marginLeft');
	out[2] = parseLength(e,str);
	str = getAttribute(e,'marginTop');
	out[3] = parseLength(e,str);
	
	
	
	//Calculate the x and y with which the element is placed in its parent.
	var align = getAttribute(e,'alignSelf');
	if (align == 'auto' || align == '') {
		align = pAlign;
	}
	str = getAttribute(e,'margin');
	if (str.indexOf('auto') > -1) {
		out[2] = (e.parentElement.offsetWidth - out[0])/2;
	} else if (align == 'center') {
		out[2] = (e.parentElement.offsetWidth - out[0])/2 + out[2];
		//out[3] = (e.parentElement.offsetHeight - out[1])/2 + out[3];
	}
	if (getAttribute(e,'cssFloat') == 'right') {
		out[2] = out[2] + (e.parentElement.offsetWidth - out[0]);
	}
	
	if (position!='absolute')
		out[3] += y;
	//TODO parse left and top attributes
	
	var tAlign = getAttribute(e,'textAlign');
	if (tAlign != '')
		align = tAlign;
	var i = 0;
	var totalX = 0;
	var totalY = 0;
	while(i< e.childElementCount) {
		var scan = scanElement((e.children)[i], align, totalX, totalY);
		if (scan.moved == false) {
			out[6+i] = scan.element;
			
			totalX = scan.offX;
			totalY = scan.offY;
			i = i + 1;
		}
	}
	out[5] = e.childElementCount;
	var newOffX = 0;
	var newOffY = 0;
	if (position != 'absolute') {
		newOffX = out[0] + out[2];
		newOffY = out[1] + out[3];
	}
	return {element: out, moved: wasMoved, offX: newOffX, offY: newOffY};
}

var elem;
elem = document.body;
elem.style.position = 'relative';
var out = scanElement(elem, "stretch", 0, 0);
out.element;
//[1,1,0,0,""+elem.firstElementChild.classList,0,[]];
//[1,1,0,0,""+document.styleSheets[0].cssRules[618].cssText,0,[]]