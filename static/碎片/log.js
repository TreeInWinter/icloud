//log 对象
;
(function(window) {
	var syslog = {
		version: "1.0",
		debug_enable: true,
		debug_level: 4,
        
        ERROR: 1,
        WARNING: 2,
        INFO : 3,
        DEBUG: 4,

		init: function() {
			//empty
		},

		print_log: function() {
			if (this.debug_enable && console) {
                for(var i = 0; i < arguments[0].length;i++){
                    console.log(arguments[0][i]);
                }
			}
		},
		debug: function() {
			if (this.debug_level >= 4) {
				this.print_log(arguments);
			}
		},
		info: function() {
			if (this.debug_level >= 3) {
				this.print_log(arguments);
			}
		},

		warning: function() {
			if (this.debug_level >= 2) {
				this.print_log(arguments);
			}
		},

		error: function() {
			if (this.debug_level >= 1) {
				this.print_log(arguments);
			}
		},

		log: function() {
			var idx = 0;
            var args = Array.prototype.slice.call(arguments);
            var log_level_string = args[0];
            var log_level = 9999;

            args = args.slice(1); 
           
            if(this[log_level_string]){
                log_level = this[log_level_string];
            }

			if (this.debug_enable && this.debug_level >= log_level) {
                this.print_log(args);
			}
		}
	}
	window.syslog = syslog;
})(window);
