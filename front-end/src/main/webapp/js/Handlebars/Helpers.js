const Helpers = (function() {

  const registerHelpers = function() {


    Handlebars.registerHelper('render-item', function(item, bins, options) {

      const match = /([^=]*)=q(\d)/.exec(item);

      if(match === null) {
        return new Handlebars.SafeString(item);
      }

      const attributeName = match[1];
      const binIndex = parseInt(match[2]);


      if(bins.hasOwnProperty(attributeName)) {

        var binLow = bins[attributeName][binIndex - 1];
        var binHigh = bins[attributeName][binIndex];

        //check if float
        if( (binLow % 1 !== 0) || (binHigh % 1 !== 0) ) {
          binLow = parseFloat(binLow).toPrecision(2);
          binHigh = parseFloat(binHigh).toPrecision(2);
        }

        return new Handlebars.SafeString(
          attributeName + " is " + binLow + " - " + binHigh
        );
      } else {
        return new Handlebars.SafeString(item);
      }
    });

    Handlebars.registerHelper('rate-bin-selector', function(rateBins, options) {
      var out  = '<ul class="nav nav-tabs">';

      for(var i=0; i < rateBins.length - 1; i++) {
        const rateLowInMinutes = (parseFloat(rateBins[i])).toPrecision(2);
        const rateHighInMinutes = (parseFloat(rateBins[i + 1])).toPrecision(2);

        const onClick = 'RateSelectorController.selectBin( $(\'#binSelector_' + i + '\'),' + i +  ')';

        //const activeStatus = i === rateBins.length - 2 ? "class='active'" : "";

        out += '<li id="binSelector_' + i + '" onclick="' + onClick + '"><a href="#"> Rate is ' + rateLowInMinutes + " - " + rateHighInMinutes + " % / s" + '</a></li>';
      }

      return out + '</ul>';
    });

  };

  return {
    registerHelpers: registerHelpers
  };

})();

