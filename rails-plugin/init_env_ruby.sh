#!/bin/bash

# Installs and updates rvm
curl -L https://get.rvm.io | bash -s head --autolibs=4
[[ -s "$HOME/.rvm/scripts/rvm" ]] && source "$HOME/.rvm/scripts/rvm"
# Install ruby-2 integrating with OS pkg mgr (apt/brew)
rvm install 2.0.0 --autolibs=4
rvm use 2.0.0-p0
rvm --default use 2.0.0-p0
rvm gemset create play-rails-bridge
rvm gemset use play-rails-bridge
gem install rails -v '4.0.0'
bundle install
rvm --rvmrc use 2.0.0-p0@play-rails-bridge
rvm use 2.0.0-p0@play-rails-bridge
