/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from "react";
import clsx from "clsx";
import styles from "./HomepageFeatures.module.css";
import MavenDependenciesImageUrl from '@site/static/img/maven_dependencies.png';
import EasyToUseImageUrl from '@site/static/img/easy_to_use.png';
import BuildFailureImageUrl from '@site/static/img/build_failure.png';



type FeatureItem = {
  title: string;
  image: string;
  description: JSX.Element;
};

const FeatureList: FeatureItem[] = [
  {
    title: "Lightweight",
    image: MavenDependenciesImageUrl,
    description: (
      <>Rely on Java standard APIs, no extra dependency required. Optional ICU4J integration.</>
    ),
  },
  {
    title: "Easy to Use",
    image: EasyToUseImageUrl,
    description: <>Provides a fluent API to load and format localized messages</>,
  },
  {
    title: "Safer",
    image: BuildFailureImageUrl,
    description: (
      <>
        Catch issues at build time! Minimize runtime errors with strong typing and message format
        validation.
      </>
    ),
  },
];

function Feature({ title, image, description }: FeatureItem) {
  return (
    <div className={clsx("col col--4")}>
      <div className="text--center">
        <img className={styles.featureSvg} alt={title} src={image} />
      </div>
      <div className="text--center padding-horiz--md">
        <h3>{title}</h3>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures(): JSX.Element {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
