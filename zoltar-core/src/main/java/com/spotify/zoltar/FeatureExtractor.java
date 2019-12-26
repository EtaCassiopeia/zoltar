/*
 * Copyright (C) 2019 Spotify AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spotify.zoltar;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import com.spotify.zoltar.FeatureExtractFns.ExtractFn;

/**
 * Functional interface for feature extraction. Should be used together with {@link Predictor}. In
 * most cases you should use the static factory methods.
 *
 * @param <ModelT> underlying type of the {@link Model}.
 * @param <InputT> type of the input to feature extraction.
 * @param <VectorT> type of feature extraction result.
 */
@FunctionalInterface
public interface FeatureExtractor<ModelT extends Model<?>, InputT, VectorT> {

  /**
   * Creates an extractor given a generic {@link ExtractFn}, consider using <a
   * href="https://github.com/spotify/featran">Featran</a> and FeatranExtractFns whenever possible.
   *
   * @param fn {@link ExtractFn} extraction function
   * @param <InputT> type of the input to feature extraction.
   * @param <VectorT> type of feature extraction result.
   */
  @SuppressWarnings("checkstyle:LineLength")
  static <ModelT extends Model<?>, InputT, VectorT>
      FeatureExtractor<ModelT, InputT, VectorT> create(final ExtractFn<InputT, VectorT> fn) {
    return (model, inputs) -> fn.apply(inputs);
  }

  /** Functional interface. Perform the feature extraction given the input. */
  CompletionStage<List<Vector<InputT, VectorT>>> extract(ModelT model, List<InputT> input);

  default <C extends FeatureExtractor<ModelT, InputT, VectorT>> C with(
      final Function<FeatureExtractor<ModelT, InputT, VectorT>, C> fn) {
    return fn.apply(this);
  }
}
