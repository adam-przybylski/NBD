FROM mongo:6-jammy

USER root

# Copy the keyFile to the correct location
COPY --chown=999 --chmod=600 keyFile /etc/mongo/keyFile
RUN chmod 400 /etc/mongo/keyFile


# Copy the mongod.conf file to the correct location
COPY --chown=999 --chmod=600 mongod.conf /etc/mongod.conf


# Use the custom configuration file
CMD ["mongod", "--config", "/etc/mongod.conf"]