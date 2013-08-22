play-rails-session-bridge
=========================

A bridge between Rails sessions and Play! sessions

play plugin Setup:
============

You should publish the play plugin local before starting using it by running this command

    $ cd play-plugin/project-code
    $ play 
    > publish-local

Then you should add the play plugin of play-rails-sesssion-bridge to your project dependancies by adding this
line to the build.scala script

    "play-rails-sesssion-bridge" % "play-rails-sesssion-bridge_2.10" % "1.0-SNAPSHOT"
    
You should create play.plugins file in the conf directory of your project and add this line to it

   
    5000:com.cloud9ers.play2.memcached.MemcachedPlugin
    
Then you should add the memcached host to application.conf

    ################ Memcached Configuration ##################
    memcached.host="127.0.0.1:11211"
    ########## OR for multiple hosts ######################
    # memcached.1.host="mumocached1:11211"
    # memcached.2.host="mumocached2:11211"

After that you can access the rails session by


    request.globalSession        #session is a JsObject
   
also you should add this import 

  
    import com.cloud9ers.play2.memcached.MemcachedPlugin._
   
Rails Plugin Setup:
===================
Add this line to your gemfile

    gem  'memcached_json_store', :git => 'git@github.com:cloud9ers/play-rails-session-bridge.git'
    
You should configure your app to use memcachedJsonStore by setting the session_sore like that:

open “config/initializers/session_store.rb”

Add these lines to it

    require 'action_dispatch/middleware/session/memcached_store'
    Rails.application.config.session_store :memcached_store, :memcache_server => ['localhost'],
                                  :namespace => 'sessions', :key => '_foundation_session', :expire_after => 7.days, :domain => :all



