const Main = (function() {

	var main = function() {

        $(document).ready(function() {
            $('[data-toggle="popover"]').popover();
        });

        Helpers.registerHelpers();
        RuleContainer.init($("#rules"));
		SearchFormController.init('rule-search-form');
	};

	return {
		main: main
	};
}());
