-- Description: update screen permissions according to #1532

update sec_permission set target = substring(target from 3) where type = 10;
