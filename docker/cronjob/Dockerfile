FROM python:3.8-slim-buster

WORKDIR /app

COPY requirements.txt ./

RUN pip3 install --upgrade pip setuptools wheel; \
    pip3 install -r ./requirements.txt; \
    apt-get update; \
    apt-get -y install openssh-server net-tools;

COPY *.py ./

RUN chmod -v +x /app/*.py;

CMD ["python3", "/app/app.py"]

