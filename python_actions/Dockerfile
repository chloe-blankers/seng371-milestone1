# The following sources were used for the docker container:
#
# [1] Github Contributors. "Creating a Docker container action."
# docs.github.com. Available: https://docs.github.com/en/actions/creating-actions/creating-a-docker-container-action
# [2] Docker Contributors. "Build your Python Image." docs.docker.com.
# Available: https://docs.docker.com/language/python/build-images/

FROM python:3.8-slim-buster

COPY read_codeowners.py /read_codeowners.py
COPY CI_CODEOWNERS /CI_CODEOWNERS

CMD ["/read_codeowners.py"]
ENTRYPOINT ["python"]