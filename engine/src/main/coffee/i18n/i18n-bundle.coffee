# (C) Uri Wilensky. https://github.com/NetLogo/Tortoise

EN_US = require('./en_us')

# Manually importing these is kind-of a pain, but it's easy enough to keep updated and I don't feel like wrestling with
# getting the dyanmic `require()` working with Tortoise's packaging.  *TODO*: Once we've switched over to ES modules
# would be a good time to revisit this.  -Jeremy B March 2023
ZH_CN = require('./zh_cn')

BUNDLES = {
  'en_us': EN_US
, 'zh_cn': ZH_CN
}

{ exceptionFactory: exceptions } = require('util/exception')

# At the moment this doesn't do much but it'd be a good place to add
# the ability to swap the current locale as needed.
# -Jeremy B November 2020

class I18nBundle

  _current = null

  constructor: () ->
    @_current = EN_US

  get: (key, args...) ->
    bundle = if @_current.hasOwnProperty(key)
      @_current
    else if @_current isnt EN_US and EN_US.hasOwnProperty(key)
      EN_US
    else
      throw exceptions.internal("Could not find a message for this key: #{key}")

    message = bundle[key]
    message(args...)

  switch: (locale) ->
    l = locale.toLowerCase()
    if BUNDLES.hasOwnProperty(l)
      @_current = BUNDLES[l]
    else
      console.warn("Unsupported locale '#{l}', reverting to 'en_us'.")
      @_current = EN_US

module.exports = I18nBundle
