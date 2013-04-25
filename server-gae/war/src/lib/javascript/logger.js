var cloudsdk = cloudsdk || {};
cloudsdk.trace = {};


/**
 * The Logger is an object used for logging debug messages. Loggers are normally named, using a hierarchical
 * dot-separated namespace. Logger names can be arbitrary strings, but they should normally be based on the package name
 * or class name of the logged component, such as cloudsdk.trace.BrowserChannel.
 * <p>
 * The Logger object is loosely based on the java class java.util.logging.Logger. It supports different levels of
 * filtering for different loggers.
 * <p>
 * The logger object should never be instantiated by application code. It should always use the cloudsdk.trace.getLogger
 * function.
 *
 * @constructor
 * @param {string}
 *          name The name of the Logger.
 */
cloudsdk.trace.Logger = function(name) {
  'use strict';

  /**
   * Name of the Logger. Generally a dot-separated namespace
   *
   * @type {string}
   * @private
   */
  this.name_ = name;
  this.startTime_ = null;
};


/**
 * Level that this logger only filters above. Null indicates it should inherit from the LogManager.
 *
 * @type {cloudsdk.trace.Logger.Level}
 * @private
 */
cloudsdk.trace.Logger.prototype.level_ = null;


/**
 * The Level class defines a set of standard logging levels that can be used to control logging output. The logging
 * Level objects are ordered and are specified by ordered integers. Enabling logging at a given level also enables
 * logging at all higher levels.
 * <p>
 * Clients should normally use the predefined Level constants such as Level.ERROR.
 * <p>
 * The levels in descending order are:
 * <ul>
 * <li>ERROR (highest value)
 * <li>WARNING
 * <li>INFO
 * <li>DEBUG
 * <li>TRACE
 * </ul>
 * In addition there is a level OFF that can be used to turn off logging, and a level ALL that can be used to enable
 * logging of all messages.
 *
 * @param {string}
 *          name The name of the level.
 * @param {number}
 *          value The numeric value of the level.
 * @constructor
 */
cloudsdk.trace.Logger.Level = function(name, value) {
  'use strict';
  /**
   * The name of the level
   *
   * @type {string}
   */
  this.name = name;

  /**
   * The numeric value of the level
   *
   * @type {number}
   */
  this.value = value;
};


/**
 * Returns string representation of the logger level.
 *
 * @return {string} the name of the Level.
 */
cloudsdk.trace.Logger.Level.prototype.toString = function() {
  'use strict';
  return this.name;
};


/**
 * Returns level from a levelName, default DEBUG.
 *
 * @param {string} levelName the name of the level.
 * @return {cloudsdk.trace.Logger.Level} the Level.
 */
cloudsdk.trace.Logger.Level.fromString = function(levelName) {
  'use strict';
  var level;
  switch (levelName.toUpperCase()) {
  case 'ALL':
    level = cloudsdk.trace.Logger.Level.ALL;
    break;
  case 'TRACE':
    level = cloudsdk.trace.Logger.Level.TRACE;
    break;
  case 'DEBUG':
    level = cloudsdk.trace.Logger.Level.DEBUG;
    break;
  case 'INFO':
    level = cloudsdk.trace.Logger.Level.INFO;
    break;
  case 'WARNING':
  case 'WARN':
    level = cloudsdk.trace.Logger.Level.WARNING;
    break;
  case 'ERROR':
    level = cloudsdk.trace.Logger.Level.ERROR;
    break;
  case 'OFF':
    level = cloudsdk.trace.Logger.Level.OFF;
    break;
  default:
    level = cloudsdk.trace.Logger.Level.DEBUG;
  }

  return level;
};


/**
 * OFF is a special level that can be used to turn off logging. This level is initialized to
 * <CODE>Number.MAX_VALUE</CODE>.
 *
 * @type {!cloudsdk.trace.Logger.Level}
 */
cloudsdk.trace.Logger.Level.OFF = new cloudsdk.trace.Logger.Level('OFF', Infinity);


/**
 * ERROR is a message level indicating a serious failure. This level is initialized to <CODE>1000</CODE>.
 *
 * @type {!cloudsdk.trace.Logger.Level}
 */
cloudsdk.trace.Logger.Level.ERROR = new cloudsdk.trace.Logger.Level('ERROR', 1000);


/**
 * WARNING is a message level indicating a potential problem. This level is initialized to <CODE>900</CODE>.
 *
 * @type {!cloudsdk.trace.Logger.Level}
 */
cloudsdk.trace.Logger.Level.WARNING = new cloudsdk.trace.Logger.Level('WARNING', 900);


/**
 * INFO is a message level for informational messages. This level is initialized to <CODE>800</CODE>.
 *
 * @type {!cloudsdk.trace.Logger.Level}
 */
cloudsdk.trace.Logger.Level.INFO = new cloudsdk.trace.Logger.Level('INFO ', 800);


/**
 * DEBUG is a message level providing tracing information. This level is initialized to <CODE>500</CODE>.
 *
 * @type {!cloudsdk.trace.Logger.Level}
 */
cloudsdk.trace.Logger.Level.DEBUG = new cloudsdk.trace.Logger.Level('DEBUG', 500);


/**
 * TRACE indicates a fairly detailed tracing message. This level is initialized to <CODE>400</CODE>.
 *
 * @type {!cloudsdk.trace.Logger.Level}
 */
cloudsdk.trace.Logger.Level.TRACE = new cloudsdk.trace.Logger.Level('TRACE', 400);


/**
 * ALL indicates that all messages should be logged. This level is initialized to <CODE>Number.MIN_VALUE</CODE>.
 *
 * @type {!cloudsdk.trace.Logger.Level}
 */
cloudsdk.trace.Logger.Level.ALL = new cloudsdk.trace.Logger.Level('ALL', 0);


/**
 * Find or create a logger for a named subsystem. If a logger has already been created with the given name it is
 * returned. Otherwise a new logger is created. If a new logger is created its log level will be configured based on the
 * LogManager configuration. It will be registered in the LogManager global namespace.
 *
 * @param {string}
 *          name A name for the logger. This should be a dot-separated name and should normally be based on the package
 *          name or class name of the subsystem.
 * @return {!cloudsdk.trace.Logger} The named logger.
 */
cloudsdk.trace.getLogger = function(name) {
  'use strict';
  return cloudsdk.trace.LogManager.getLogger(name);
};


/**
 * Gets the CloudSDK default logger.
 * @return {cloudsdk.trace.Logger} t
 * he logger.
 */
cloudsdk.trace.getDefaultLogger = function() {
	'use strict';
	if (cloudsdk.trace.defaultLogger_) {
		return cloudsdk.trace.defaultLogger_;
	}
	cloudsdk.trace.defaultLogger_ = cloudsdk.trace.getLogger('cloudsdk');
	return cloudsdk.trace.defaultLogger_;
};


/**
 * Gets the name of this logger.
 *
 * @return {string} The name of this logger.
 */
cloudsdk.trace.Logger.prototype.getName = function() {
  'use strict';
  return this.name_;
};


/**
 * Set the log level specifying which message levels will be logged by this logger. Message levels lower than this value
 * will be discarded. The level value Level.OFF can be used to turn off logging. If the new level is null, it means that
 * this node should inherit its level from its nearest ancestor with a specific (non-null) level value.
 *
 * @param {cloudsdk.trace.Logger.Level}
 *          level The new level.
 */
cloudsdk.trace.Logger.prototype.setLevel = function(level) {
  'use strict';

  this.level_ = level;
};


/**
 * Gets the log level specifying which message levels will be logged by this logger. Message levels lower than this
 * value will be discarded. The level value Level.OFF can be used to turn off logging. If the level is null, it means
 * that this node should inherit its level from its nearest ancestor with a specific (non-null) level value.
 *
 * @return {cloudsdk.trace.Logger.Level} The level.
 */
cloudsdk.trace.Logger.prototype.getLevel = function() {
  'use strict';

  return this.level_;
};


/**
 * Check if a message of the given level would actually be logged by this logger.
 *
 * @param {cloudsdk.trace.Logger.Level}
 *          level The level to check.
 * @return {boolean} Whether the message would be logged.
 */
cloudsdk.trace.Logger.prototype.isLoggable = function(level) {
  'use strict';
  if (this.level_ && (level.value < this.level_.value)) {
    return false;
  }
  if (level.value < cloudsdk.trace.LogManager.rootLevel_.value) {
    return false;
  }
  return true;
};


/**
 * Log a message. If the logger is currently enabled for the given message level then the given message is forwarded to
 * all the registered output Handler objects.
 *
 * @param {cloudsdk.trace.Logger.Level}
 *          level One of the level identifiers.
 * @param {string}
 *          msg The string message.
 * @param {Error|Object=}
 *          opt_object An object or exception associated with the message.
 */
cloudsdk.trace.Logger.prototype.log = function(level, msg, opt_object) {
  'use strict';
  // java caches the effective level, not sure it's necessary here
  if (this.isLoggable(level)) {
    this.doLogRecord_(this.getLogRecord(level, msg, opt_object));
  }
};


/**
 * Creates a new log record and adds the optional object (if present) to it.
 *
 * @param {cloudsdk.trace.Logger.Level}
 *          level One of the level identifiers.
 * @param {string}
 *          msg The string message.
 * @param {Error|Object=}
 *          opt_object An object or exception associated with the message.
 * @return {!cloudsdk.trace.LogRecord} A log record.
 */
cloudsdk.trace.Logger.prototype.getLogRecord = function(level, msg, opt_object) {
  'use strict';
  var logRecord = new cloudsdk.trace.LogRecord(level, String(msg), this.name_);
  if (opt_object) {
    logRecord.setObject(opt_object);
  }
  return logRecord;
};


/**
 * Log a message at the Logger.Level.ERROR level. If the logger is currently enabled for the given message level then
 * the given message is forwarded to all the registered output Handler objects.
 *
 * @param {string}
 *          msg The string message.
 * @param {Object|Error=}
 *          opt_object An object or exception associated with the message.
 */
cloudsdk.trace.Logger.prototype.error = function(msg, opt_object) {
  'use strict';
  this.log(cloudsdk.trace.Logger.Level.ERROR, msg, opt_object);
};


/**
 * Log a message at the Logger.Level.WARNING level. If the logger is currently enabled for the given message level then
 * the given message is forwarded to all the registered output Handler objects.
 *
 * @param {string}
 *          msg The string message.
 * @param {Object|Error=}
 *          opt_object An object or exception associated with the message.
 */
cloudsdk.trace.Logger.prototype.warning = function(msg, opt_object) {
  'use strict';
  this.log(cloudsdk.trace.Logger.Level.WARNING, msg, opt_object);
};


/**
 * Log a message at the Logger.Level.INFO level. If the logger is currently enabled for the given message level then the
 * given message is forwarded to all the registered output Handler objects.
 *
 * @param {string}
 *          msg The string message.
 * @param {Object|Error=}
 *          opt_object An object or exception associated with the message.
 */
cloudsdk.trace.Logger.prototype.info = function(msg, opt_object) {
  'use strict';
  this.log(cloudsdk.trace.Logger.Level.INFO, msg, opt_object);
};


/**
 * Log a message at the Logger.Level.DEBUG level. If the logger is currently enabled for the given message level then
 * the given message is forwarded to all the registered output Handler objects.
 *
 * @param {string}
 *          msg The string message.
 * @param {Object|Error=}
 *          opt_object An object or exception associated with the message.
 */
cloudsdk.trace.Logger.prototype.debug = function(msg, opt_object) {
  'use strict';
  this.log(cloudsdk.trace.Logger.Level.DEBUG, msg, opt_object);
};


/**
 * Log a message at the Logger.Level.TRACE level. If the logger is currently enabled for the given message level then
 * the given message is forwarded to all the registered output Handler objects.
 *
 * @param {string}
 *          msg The string message.
 * @param {Object|Error=}
 *          opt_object An object or exception associated with the message.
 */
cloudsdk.trace.Logger.prototype.trace = function(msg, opt_object) {
  'use strict';
  this.log(cloudsdk.trace.Logger.Level.TRACE, msg, opt_object);
};


/**
 * Dump given variable to the log.
 *
 * @param {Object} data .
 * @param {Boolean=} opt_shallow shallow or deep. deep is default.
 * @param {cloudsdk.trace.Logger.Level=} opt_level log level, defaults to TRACE.
 */
cloudsdk.trace.Logger.prototype.varDump = function(data, opt_shallow, opt_level) {
  'use strict';
  var i = null;
  opt_level = opt_level || cloudsdk.trace.Logger.Level.TRACE;

  if (data === null || data === undefined) {
    this.log(opt_level, 'VarDump:' + data);
    return;
  }

  try {
    opt_shallow = opt_shallow === true;

    for (i in data) {
      if (data.hasOwnProperty(i)) {
        this.log(opt_level, i + ':[' + typeof (data[i]) + ']' + data[i]);

        if (!opt_shallow &&
            (typeof data[i] === 'object' || typeof data[i] === 'function')) {
          this.varDump(data[i], opt_shallow, opt_level);
        }
      }
    }
  } catch (e) {
    this.error('Failed to dump variable', e);
  }
};


/**
 * Log a start message at the Logger.Level.DEBUG level and store the start time in the Logger object. Every Logger can
 * only measure one timer. If the logger is currently enabled for the given message level then the given message is
 * forwarded to all the registered output Handler objects.
 *
 * @param {string}
 *          msg The string message.
 * @param {Object|Error=}
 *          opt_object An object or exception associated with the message.
 */
cloudsdk.trace.Logger.prototype.start = function(msg, opt_object) {
  'use strict';
  if (this.startTime_) {
    this.warning('Overwrites old start time: ' + this.startTime_);
  }
  this.startTime_ = new Date();
  this.log(cloudsdk.trace.Logger.Level.DEBUG, 'START: ' + msg, opt_object);
};


/**
 * Log a stop message at the Logger.Level.DEBUG level and show the time since the corresponding start call. Every Logger
 * can only measure one timer. If the logger is currently enabled for the given message level then the given message is
 * forwarded to all the registered output Handler objects.
 *
 * @param {string}
 *          msg The string message.
 * @param {Object|Error=}
 *          opt_object An object or exception associated with the message.
 */
cloudsdk.trace.Logger.prototype.stop = function(msg, opt_object) {
  'use strict';
  var stopTime = new Date(), time;

  time = stopTime - this.startTime_;
  this.startTime_ = null;
  this.log(cloudsdk.trace.Logger.Level.DEBUG, 'STOP with: ' + time + 'ms. ' + msg,
      opt_object);
};


/**
 * Log a LogRecord.
 *
 * @param {cloudsdk.trace.LogRecord}
 *          logRecord A log record to log.
 * @private
 */
cloudsdk.trace.Logger.prototype.doLogRecord_ = function(logRecord) {
  'use strict';
  var i, handler, rootHandlers = cloudsdk.trace.LogManager.rootHandlers_, len = rootHandlers.length, filter = cloudsdk.trace.LogManager.rootFilter;
  if (filter !== null) {
    if (!this.filter_(logRecord, filter)) {
      return;
    }
  }
  for (i = 0; i < len; i++) {
    handler = rootHandlers[i];
    handler(logRecord);
  }
};


/**
 * Filter a LogRecord.
 *
 * @param {cloudsdk.trace.LogRecord} logRecord A log record to log.
 * @param {string} filter a filter (regular expression).
 * @return {boolean} true if the statement should be logged.
 * @private
 */
cloudsdk.trace.Logger.prototype.filter_ = function(logRecord, filter) {
  'use strict';
  var re = new RegExp(filter);
  //TODO
  if (re.test(logRecord.level_)) {
    return true;
  }
  if (re.test(logRecord.loggerName_)) {
    return true;
  }
  if (re.test(logRecord.msg_)) {
    return true;
  }
  return false;
};


/**
 * LogRecord objects are used to pass logging requests between the logging framework and individual log Handlers.
 *
 * @constructor
 * @param {cloudsdk.trace.Logger.Level}
 *          level One of the level identifiers.
 * @param {string}
 *          msg The string message.
 * @param {string}
 *          loggerName The name of the source logger.
 */
cloudsdk.trace.LogRecord = function(level, msg, loggerName) {
  'use strict';
  this.time_ = +new Date();
  this.level_ = level;
  this.msg_ = msg;
  this.loggerName_ = loggerName;
};


/**
 * Set an object, normally an exception, that is part of the log record.
 *
 * @param {Object}
 *          object the object.
 */
cloudsdk.trace.LogRecord.prototype.setObject = function(object) {
  'use strict';
  this.object_ = object;
};


/**
 * Get the source Logger's name.
 *
 * @return {string} source logger name (may be null).
 */
cloudsdk.trace.LogRecord.prototype.getLoggerName = function() {
  'use strict';
  return this.loggerName_;
};


/**
 * Get the object (normally an exception) that is part of the log record.
 *
 * @return {Object} the object.
 */
cloudsdk.trace.LogRecord.prototype.getObject = function() {
  'use strict';
  return this.object_;
};


/**
 * Get the logging message level, for example Level.SEVERE.
 *
 * @return {cloudsdk.trace.Logger.Level} the logging message level.
 */
cloudsdk.trace.LogRecord.prototype.getLevel = function() {
  'use strict';
  return this.level_;
};


/**
 * Get the "raw" log message, before localization or formatting.
 *
 * @return {string} the raw message string.
 */
cloudsdk.trace.LogRecord.prototype.getMessage = function() {
  'use strict';
  return this.msg_;
};


/**
 * Get event time in milliseconds since 1970.
 *
 * @return {number} event time in millis since 1970.
 */
cloudsdk.trace.LogRecord.prototype.getMillis = function() {
  'use strict';
  return this.time_;
};


/**
 * There is a single global LogManager object that is used to maintain a set of shared state about Loggers and log
 * services. This is loosely based on the java class java.util.logging.LogManager.
 */
cloudsdk.trace.LogManager = {};


/**
 * List of all appenders.
 *
 * @type {!Array.<Function>}
 * @private
 */
cloudsdk.trace.LogManager.rootHandlers_ = [];


/**
 * Log level.
 *
 * @type {cloudsdk.trace.Logger.Level}
 * @private
 */
cloudsdk.trace.LogManager.rootLevel_ = cloudsdk.trace.Logger.Level.DEBUG;


/**
 * Map of logger names to logger objects
 *
 * @type {!Object}
 * @private
 */
cloudsdk.trace.LogManager.loggers_ = {};


/**
 * The root logger which is the root of the logger tree.
 *
 * @type {cloudsdk.trace.Logger}
 * @private
 */
cloudsdk.trace.LogManager.rootLogger_ = null;


/**
 * The root filter which filters all log messages.
 *
 * @type {string}
 * @private
 */
cloudsdk.trace.LogManager.rootFilter_ = null;


/**
 * Initialize the LogManager if not already initialized
 */
cloudsdk.trace.LogManager.initialize = function() {
  'use strict';
  if (!cloudsdk.trace.LogManager.rootLogger_) {
    cloudsdk.trace.LogManager.rootLogger_ = cloudsdk.trace.LogManager.createLogger_('');
    cloudsdk.trace.LogManager.setLevel(cloudsdk.trace.Logger.Level.DEBUG);
    cloudsdk.trace.LogManager.rootHandlers_ = [];
    cloudsdk.trace.LogManager.rootFilter_ = null;
  }
};


/**
 * Returns all the loggers
 *
 * @return {!Object} Map of logger names to logger objects.
 */
cloudsdk.trace.LogManager.getLoggers = function() {
  'use strict';
  cloudsdk.trace.LogManager.initialize();
  return cloudsdk.trace.LogManager.loggers_;
};


/**
 * Returns the root of the logger tree namespace, the logger with the empty string as its name
 *
 * @return {!cloudsdk.trace.Logger} The root logger.
 */
cloudsdk.trace.LogManager.getRoot = function() {
  'use strict';
  cloudsdk.trace.LogManager.initialize();
  return (cloudsdk.trace.LogManager.rootLogger_);
};


/**
 * Method to find a named logger.
 *
 * @param {string}
 *          name A name for the logger. This should be a dot-separated name and should normally be based on the package
 *          name or class name of the subsystem.
 * @return {!cloudsdk.trace.Logger} The named logger.
 */
cloudsdk.trace.LogManager.getLogger = function(name) {
  'use strict';
  cloudsdk.trace.LogManager.initialize();
  var ret = cloudsdk.trace.LogManager.loggers_[name];
  return ret || cloudsdk.trace.LogManager.createLogger_(name);
};


/**
 * Creates the named logger
 *
 * @param {string}
 *          name The name of the logger.
 * @return {!cloudsdk.trace.Logger} The named logger.
 * @private
 */
cloudsdk.trace.LogManager.createLogger_ = function(name) {
  'use strict';
  var logger = new cloudsdk.trace.Logger(name);
  cloudsdk.trace.LogManager.loggers_[name] = logger;
  return logger;
};


/**
 * Set the log level specifying which message levels will be logged by this logger. Message levels lower than this value
 * will be discarded. The level value Level.OFF can be used to turn off logging. If the new level is null, it means that
 * this node should inherit its level from its nearest ancestor with a specific (non-null) level value.
 *
 * @param {cloudsdk.trace.Logger.Level}
 *          level The new level.
 */
cloudsdk.trace.LogManager.setLevel = function(level) {
  'use strict';
  cloudsdk.trace.LogManager.rootLevel_ = level;
};


/**
 * Return the root level.
 *
 * @return {cloudsdk.trace.Logger.Level} level of root logger.
 */
cloudsdk.trace.LogManager.getLevel = function() {
  'use strict';
  return cloudsdk.trace.LogManager.rootLevel_;
};


/**
 * Sets the root filter.
 * Set to null to disable filtering.
 * @param {string} filter the new filter.
 */
cloudsdk.trace.LogManager.setFilter = function(filter) {
  'use strict';
  cloudsdk.trace.LogManager.rootFilter_ = filter;
};


/**
 * Return the root filter.
 *
 * @return {string} filter of root logger.
 */
cloudsdk.trace.LogManager.getFilter = function() {
  'use strict';
  return cloudsdk.trace.LogManager.rootFilter_;
};


/**
 * Adds a handler to the logger. This doesn't use the event system because we want to be able to add logging to the
 * event system. Can not add the same handler a second time.
 *
 * @param {Function}
 *          handler Handler function to add.
 */
cloudsdk.trace.LogManager.addHandler = function(handler) {
  'use strict';
  cloudsdk.trace.LogManager.initialize();
  var i = cloudsdk.trace.LogManager.findHandler_(handler);
  if (i < 0) {
    cloudsdk.trace.LogManager.rootHandlers_.push(handler);
  }
};


/**
 * Removes a handler from the logger. This doesn't use the event system because we want to be able to add logging to the
 * event system.
 *
 * @param {Function}
 *          handler Handler function to remove.
 * @return {Array.Function} the remaining handlers.
 */
cloudsdk.trace.LogManager.removeHandler = function(handler) {
  'use strict';
  cloudsdk.trace.LogManager.initialize();
  var i = cloudsdk.trace.LogManager.findHandler_(handler);
  if (i >= 0) {
    return cloudsdk.trace.LogManager.rootHandlers_.splice(i, 1);
  }
  return [];
};


/**
 * Finds a handler.
 *
 * @param {Function}
 *          handler Handler function to add.
 * @return {number} index of found element or -1.
 * @private
 */
cloudsdk.trace.LogManager.findHandler_ = function(handler) {
  'use strict';
  var handlers = cloudsdk.trace.LogManager.rootHandlers_, i;
  for (i = 0; i < handlers.length; i++) {
    if (handlers[i] === handler) {
      return i;
    }
  }
  return -1;
};


/**
 * Formats a record as text
 *
 * @param {cloudsdk.trace.LogRecord}
 *          logRecord the logRecord to format.
 * @param {boolean=}
 *          opt_doNoException do not append string describing exception.
 * @return {string} The formatted string.
 * @private
 */
cloudsdk.trace.LogManager.formatRecord_ = function(logRecord, opt_doNoException) {
  'use strict';
  var date, exceptionText, sb = [];
  sb.push('Nessie: ');
  date = new Date(logRecord.getMillis());
  sb.push(date.toISOString(), ' ');
  sb.push(logRecord.getLevel().name);
  sb.push(' [', logRecord.getLoggerName(), '] ');
  sb.push(logRecord.getMessage(), '\n');
  if (!opt_doNoException && logRecord.getObject()) {
    exceptionText = cloudsdk.trace.LogManager.errorToText_(logRecord.getObject());
    sb.push(exceptionText, '\n');
  }
  return sb.join('');
};


/**
 * Normalizes the error/exception object between browsers.
 *
 * @param {Object}
 *          err Raw error object.
 * @return {string} Normalized error string.
 * @private
 */
cloudsdk.trace.LogManager.errorToText_ = function(err) {
  'use strict';
  var sb = [], stack, name, lineNumber, fileName;
  if (typeof err === 'string') {
    return 'Exception with message: ' + err;
  }
  if (err.name) {
    name = err.name;
    stack = err.stack || null;
    try {
      lineNumber = err.lineNumber || err.line || null;
    } catch (e) {
      lineNumber = null;
    }

    try {
      fileName = err.fileName || err.filename || err.sourceURL || null;
    } catch (er) {
      fileName = null;
    }
    sb.push('Exception: ', name, ' with message: ', err.message);
    if (fileName) {
      sb.push(' File:', fileName);
    }
    if (lineNumber) {
      sb.push('(', lineNumber, ') ');
    }
    if (stack) {
      sb.push(stack);
    }
  } else {
    // normal object
    sb.push('Object: ');
    sb.push(err.toString());
  }
  return sb.join('');
};


/**
 * A simple global Object to send logging entries to the console. Create and install a log handler that logs to
 * window.console if available
 */
cloudsdk.trace.ConsoleHandler = {
  console_: window.console,
  enabled: true,
  addLogRecord: function(logRecord) {
    'use strict';
    var console = window.console, record, object;
    if (console) {
      record = cloudsdk.trace.LogManager.formatRecord_(logRecord, true);
      object = logRecord.getObject();
      switch (logRecord.getLevel()) {
      case cloudsdk.trace.Logger.Level.ERROR:
        if (object) {
          console.error(record, object);
        } else {
          console.error(record);
        }
        break;
      case cloudsdk.trace.Logger.Level.WARNING:
        if (object) {
          console.warn(record, object);
        } else {
          console.warn(record);
        }
        break;
      case cloudsdk.trace.Logger.Level.INFO:
        if (object) {
          console.info(record, object);
        } else {
          console.info(record);
        }
        break;
      case cloudsdk.trace.Logger.Level.DEBUG:
      case cloudsdk.trace.Logger.Level.TRACE:
        if (object) {
          console.debug(record, object);
        } else {
          console.debug(record);
        }
        break;
      default:
        if (object) {
          console.error(record, object);
        } else {
          console.error(record);
        }
        break;
      }
    }
  }
};

// Add console handler to logger
cloudsdk.trace.LogManager.addHandler(cloudsdk.trace.ConsoleHandler.addLogRecord);


/**
 * A simple global Object to send logging entries to the efi plugin logger.
 */
cloudsdk.trace.EfiHandler = {
  enabled: true,

  // cut off level for efi log.
  level: cloudsdk.trace.Logger.Level.TRACE,

  addLogRecord: function(logRecord) {
    'use strict';

    var efi = window.efiPlugin, record;
    if (efi) {
      if (logRecord.getLevel().value < cloudsdk.trace.EfiHandler.level.value) {
        return;
      }
      record = cloudsdk.trace.LogManager.formatRecord_(logRecord, false);

      switch (logRecord.getLevel()) {
      case cloudsdk.trace.Logger.Level.ERROR:
        efi.traceError(record);
        break;
      case cloudsdk.trace.Logger.Level.WARNING:
        efi.traceWarning(record);
        break;
      case cloudsdk.trace.Logger.Level.INFO:
        efi.traceMessage(record);
        break;
      case cloudsdk.trace.Logger.Level.DEBUG:
      case cloudsdk.trace.Logger.Level.TRACE:
        efi.traceMessage(record);
        break;
      default:
        efi.traceFatalError(record);
        break;
      }
    }
  }
};

/**
 * simple handler for printing messages to the testing webpage.
 */
cloudsdk.trace.PageHandler = {
  console_: window.console,
  enabled: true,
  addLogRecord: function(logRecord) {
    'use strict';

    var console = window.console, record;
    if (console) {
      record = cloudsdk.trace.LogManager.formatRecord_(logRecord);

      switch (logRecord.getLevel()) {
      case cloudsdk.trace.Logger.Level.ERROR:
        window.document.write(record + '<br>');
        break;
      case cloudsdk.trace.Logger.Level.WARNING:
        window.document.write(record + '<br>');
        break;
      default:
        window.document.write(record + '<br>');
        break;
      }
    }
  }
};

// Add page handler to logger
// cloudsdk.trace.LogManager.addHandler(cloudsdk.trace.PageHandler.addLogRecord);

// Register global error handler
window.onerror = function(message, url, lineNumber) {
  'use strict';
  cloudsdk.trace.getDefaultLogger().error(
      message + ' on ' + url + ':' + lineNumber);
  return false;
};

/**
 * web logger test
 */

/**
 * Map of logger names to logger objects
 *
 * @type {!Object}
 * @private
 */
cloudsdk.trace.Logger.webloggers_ = {};

/**
 * gets a weblogger data object for the given key / name. if no object for the provided name is available, a dummy one will be created.
 * @param name
 * @returns {Boolean}
 */
cloudsdk.trace.Logger.prototype.getWebLogger = function(name) {
  'use strict';
  var ret = cloudsdk.trace.Logger.webloggers_[name];
  return ret || cloudsdk.trace.Logger.createLogger_(name);
};

/**
 * creates a weblogger data object.
 *
 * @param {String} name the name of the web logger
 * @param {String} id request id
 * @param {String} language the used language
 * @param {String} vin the used vin
 * @param {String} version the used version
 * @param {String} operation operation description
 * @param {String} text log message text
 * @returns {oe.data.OEWebLogData}
 */
cloudsdk.trace.Logger.prototype.createWebLogger = function(name, id, language, vin,
    version, operation, text) {
  'use strict';
  var logger = new oe.data.OEWebLogData(name, id, language, vin, version,
      operation, text);
  cloudsdk.trace.Logger.webloggers_[name] = logger;
  return logger;
};

/**
 * calls web service interface and sends a logmessage via ajax
 *
 * @param {oe.data.OEWebLogData} weblog object .
 * @param {cloudsdk.trace.Logger.Level} opt_level log level for which the message also should be logged in the default way. if not provided, the message will not be logged.
 * @param {boolean} resetTimestamps flag if the collected timestampdata should be resetted after sending the data
 */
cloudsdk.trace.Logger.prototype.webLog = function(data, opt_level, resetTimestamps) {
  'use strict';
  var SERVICE_URL = oe.ConfigManager.getInstance().getParameter('webLogUrl');
  //TODO remove, add backend logging
  //SERVICE_URL = '';
  
  if (opt_level) {
    this.log(opt_level, 'sending logmessage to the backend service: ' +
        data.generateWebLogMessage());
  }


  $.ajax({
    type: 'POST',
    url: SERVICE_URL,
    data: data.generateWebLogMessage(),
    processData: false,
    dataType: "xml",
    contentType: "text/xml; charset=\"utf-8\"",
    beforeSend: function(req) {
      req.setRequestHeader('username', "username");
      req.setRequestHeader('password', "password");
    },
    success: function(data) {
      if (resetTimestamps) {
        data.resetTimestampData();
      }
    }
  });
};
