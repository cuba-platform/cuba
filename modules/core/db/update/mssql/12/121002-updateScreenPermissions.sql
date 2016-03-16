-- Description: update screen permissions according to #1532

update sec_permission set target = substring(target, 3, len(target) - 2) where type = 10;
