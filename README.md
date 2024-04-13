# Java application to cloud journey
Welcome to our demonstration repository!

## Purpose
This repository is for learning purposes. We keep all components of our platform, including application, infrastructure, orchestration, and diagrams, within a single repository.

## GitHub Actions
All build, run, and verify scripts are in GitHub Actions workflows. You can find specific instructions within the [.github/workflows](.github/workflows) directory.

## Feedback
If you have any questions, suggestions, or feedback, feel free to open an [discussion](https://github.com/handsonarchitects/java-to-cloud-journey/discussions) or reach out to us directly.

Thank you for exploring our example repository!

— [HandsOnArchitects.com](https://handsonarchitects.com/)

## Quick Start

> Prerequisites: Docker, Java 11, Kind, Kubectl

1. Clone the repository
2. Create a Kubernetes cluster using Kind by running 
   ```bash
   demo/setup-kind.sh
   ```
3. Build the application and deploy the demo to the Kubernetes cluster by running
   ```bash
   demo/deploy.sh -b
   ```