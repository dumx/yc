/**
 * 自动匹配form表单中input标签中name值
 */
(function($) {
	jQuery.fn.formloader = function(parameters) {
		var form = $(this);
		var formFields = form.find("input[name],select[name],textarea[name]");
		formFields.each(function() {
			var name = this.name;
			var value = jQuery.proxy(function() {
				try {
					return eval('this.' + name);
				} catch (e) {
					return "";
				}
			}, parameters.data)();
			$("input[name=\"" + name + "\"][type=text]", form).val(value);
			$("input[name=\"" + name + "\"][type=hidden]", form).val(value);
			$("textarea[name=\"" + name + "\"]", form).val(value);
			$("select[name=\"" + name + "\"]", form).val(value);
			setCheckVal(name, value);
			if ($.fn.typeahead) {
				$("input[name=\"" + name + "\"][type=text]", form).typeahead('val', value);
			}
		});
		if (parameters.onLoadSuccess === "function") {
			parameters.onLoadSuccess.call(target, dd);
		}
		if($.uniform) {
			$.uniform.update();
		}
		function setCheckVal(key, val) { // TODO test
			var rr = form.find("input[name=\"" + key + "\"][type=radio], input[name=\"" + key + "\"][type=checkbox]");
			if (rr.length > 0) {
				rr.prop("checked", false);
				rr.each(function() {
					var f = $(this);
					if (f.val() == String(val) || $.inArray(f.val(), val) >= 0) {
						f.prop("checked", true);
					}
				});
			}
		}
		return form;
	};
})(jQuery);