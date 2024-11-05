#!/bin/bash

export $(cat ./.env | xargs)

echo 'export complete'