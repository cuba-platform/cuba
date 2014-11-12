#Current Date
Tdate=`date +%Y_%m_%d`
#Postgresql path
PG_DIR=/usr/lib/postgresql/8.3/bin
#Tomcat path
TOMCAT_DIR=/opt/tomcat
#Backup root folder
BACKUP_DIR=/home/pavlov/backup
#Postgresql host and port
PG_HOST=localhost
PG_PORT=5432
#Database name
DB_NAME=docflow
#DB user
PG_USER=root

if [ ! -d $BACKUP_DIR/$Tdate ]; then
    mkdir $BACKUP_DIR/$Tdate
fi

DUMP_FILE=$BACKUP_DIR/$Tdate/$DB_NAME'_'$Tdate.dump

$PG_DIR/pg_dump -i -h $PG_HOST -p $PG_PORT -Fc -f $DUMP_FILE -U $PG_USER $DB_NAME

TOMCAT_BACKUP_DIR=$BACKUP_DIR/$Tdate/tomcat

if [ ! -d $TOMCAT_BACKUP_DIR ]; then
    mkdir $TOMCAT_BACKUP_DIR
fi

cp -uR $TOMCAT_DIR/* $TOMCAT_BACKUP_DIR